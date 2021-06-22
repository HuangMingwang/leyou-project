package com.leyou.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.SkuDTO;
import com.leyou.trade.constants.BasePayConstants;
import com.leyou.trade.dto.OrderFormDTO;
import com.leyou.trade.entity.Order;
import com.leyou.trade.entity.OrderDetail;
import com.leyou.trade.entity.OrderLogistics;
import com.leyou.trade.entity.enums.OrderStatus;
import com.leyou.trade.mapper.OrderMapper;
import com.leyou.trade.service.CartService;
import com.leyou.trade.service.OrderDetailService;
import com.leyou.trade.service.OrderLogisticsService;
import com.leyou.trade.service.OrderService;
import com.leyou.trade.utils.PayHelper;
import com.leyou.trade.utils.UserHolder;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.AddressDTO;
import feign.FeignException;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.*;

import static com.leyou.common.constants.BaseMQConstants.ExchangeConstants.ORDER_EXCHANGE_NAME;
import static com.leyou.common.constants.BaseMQConstants.RoutingKeyConstants.EVICT_ORDER_KEY;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 2:15 下午
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final ItemClient itemClient;

    private final UserClient userClient;

    private final OrderDetailService detailService;

    private final OrderLogisticsService logisticsService;

    private final PayHelper payHelper;

    private final AmqpTemplate amqpTemplate;

    public OrderServiceImpl(ItemClient itemClient, UserClient userClient, OrderDetailService detailService, OrderLogisticsService logisticsService, PayHelper payHelper, AmqpTemplate amqpTemplate) {
        this.itemClient = itemClient;
        this.userClient = userClient;
        this.detailService = detailService;
        this.logisticsService = logisticsService;
        this.payHelper = payHelper;
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    @Transactional
    public Long createOrder(OrderFormDTO orderFormDTO) {
        // 1.订单
        Order order = new Order();
        // 1.1.订单金额信息，包括：商品总金额、实付金额、邮费
        Map<Long, Integer> carts = orderFormDTO.getCarts();
        // a.获取sku的id集合
        Set<Long> ids = carts.keySet();
        // b.根据sku的id查询sku
        List<SkuDTO> skuList = itemClient.querySkuByIds(new ArrayList<>(ids));
        // c.计算总金额
        long total = 0L;
        for (SkuDTO sku : skuList) {
            Integer num = carts.get(sku.getId());
            total += sku.getPrice() * num;
        }
        order.setTotalFee(total);
        order.setPostFee(0L);
        order.setActualFee(order.getTotalFee() + order.getPostFee()/*TODO 减去优惠金额*/);
        order.setPaymentType(orderFormDTO.getPaymentType());

        // 1.2.用户
        Long userId = UserHolder.getUser();
        order.setUserId(userId);

        // 1.3.状态
        order.setStatus(OrderStatus.INIT);

        // 1.4.写入数据库
        boolean isSuccess = save(order);
        if (!isSuccess) {
            throw new LyException(500, "新增订单失败！");
        }

        Long orderId = order.getOrderId();

        // 2.订单详情
        List<OrderDetail> orderDetails = new ArrayList<>(skuList.size());
        // 2.1.把sku集合封装成OrderDetail集合
        for (SkuDTO sku : skuList) {
            OrderDetail detail = buildOrderDetailFromSku(sku, orderId, carts.get(sku.getId()));
            orderDetails.add(detail);
        }
        // 2.2.写入数据库
        detailService.saveBatch(orderDetails);

        // 3.物流
        // 3.1.获取地址的id
        Long addressId = orderFormDTO.getAddressId();
        // 3.2.根据id查询地址
        AddressDTO address = userClient.queryAddressById(addressId);
        // 判断当前地址是否属于当前用户
       /* if(!Objects.equals(address.getUserId(), userId)){
            // 地址不属于当前用户，数据有误
            throw new LyException(400, "请不要瞎搞，收货地址不是你的！");
        }*/
        // 3.3.封装OrderLogistics，从address中拷贝属性到OrderLogistics
        OrderLogistics orderLogistics = address.toEntity(OrderLogistics.class);
        orderLogistics.setOrderId(orderId);

        // 3.4.写入数据库
        isSuccess = logisticsService.save(orderLogistics);
        if (!isSuccess) {
            throw new LyException(500, "新增订单失败！");
        }

        // 4.减库存
        try {
            itemClient.deductStock(carts);
        } catch (FeignException e) {
            throw new LyException(e.status(), e.contentUTF8());
        }

        // 5.发送延迟消息到交换机
        amqpTemplate.convertAndSend(ORDER_EXCHANGE_NAME, EVICT_ORDER_KEY, orderId);

        return orderId;
    }

    @Override
    public String getPayUrl(Long orderId) {
        // 查询订单
        Order order = getById(orderId);
        // 健壮性判断
        if (order == null) {
            // 订单不存在
            throw new LyException(400, "订单不存在！");
        }
        // 订单状态是否是未支付
        if (order.getStatus() != OrderStatus.INIT) {
            // 订单已支付或已关闭，无法再次支付
            throw new LyException(400, "订单已支付或已关闭");
        }
        // 统一下单，获取链接
        return payHelper.unifiedOrder(orderId, order.getActualFee());
    }

    @Transactional
    @Override
    public void handleNotify(Map<String, String> data) {
        // 1.校验签名
        payHelper.checkSignature(data);
        // 2.校验returnCode
        payHelper.checkReturnCode(data);
        // 3.校验ResultCode
        payHelper.checkResultCode(data);

        // 4.校验订单金额
        // 4.1.获取结果中的订单id和订单金额
        String orderIdStr = data.get(BasePayConstants.ORDER_NO_KEY);
        String totalFee = data.get(BasePayConstants.TOTAL_FEE_KEY);
        if (StringUtils.isBlank(orderIdStr) || StringUtils.isBlank(totalFee)) {
            // 请求数据有误
            throw new LyException(400, "订单id或支付金额为空！");
        }
        // 4.2.查询数据库中的订单
        Long orderId = Long.valueOf(orderIdStr);
        Order order = getById(orderId);
        // 4.3.比较金额
        if (!StringUtils.equals(totalFee, order.getActualFee().toString())) {
            // 金额不匹配，错误数据
            throw new LyException(400, "订单金额有误，再捣乱就报警了！");
        }

        // 5.幂等处理（注意多线程并发安全）
        if(order.getStatus() != OrderStatus.INIT){
            // 订单已支付，认为是重复通知，返回成功
            return;
        }

        // 6.更新订单状态（注意多线程并发安全）
        // update tb_order set status = 2, pay_time = NOW() where order_id = 45154151 AND status = 1
        update()
                .set("status", OrderStatus.PAY_UP.getValue())
                .set("pay_time", new Date())
                .eq("order_id", orderId)
                .eq("status", OrderStatus.INIT.getValue())
                .update();
    }

    @Override
    public Integer queryOrderState(Long orderId) {
        // 查询订单
        Order order = getById(orderId);
        // 判断是否存在
        if (order == null) {
            throw new LyException(400, "订单不存在！");
        }
        return order.getStatus().getValue();
    }

    @Override
    @Transactional
    public void evictOrder(Long orderId) {
        // 1.根据id查询订单状态
        Order order = getById(orderId);
        if(order == null){
            // 订单不存在，无需处理
            return;
        }
        // 2.判断是否是未支付
        if(order.getStatus() != OrderStatus.INIT) {
            //  - 如果已支付或已结束，则无需处理
            return;
        }
        // 3.如果未支付则需要关闭订单，设置状态为5（已关闭），注意**幂等处理**
        boolean success = update()
                .set("status", OrderStatus.CLOSED.getValue())
                .set("close_time", new Date())
                .eq("order_id", orderId)
                .eq("status", OrderStatus.INIT.getValue())
                .update();
        if(!success){
            // 关闭失败，无需继续
            return;
        }
        log.info("订单{}已关闭", orderId);
        // 4.如果关闭订单了，还要查询对应OrderDetail，
        List<OrderDetail> details = detailService.query().eq("order_id", orderId).list();
        Map<Long, Integer> skuMap = new HashMap<>();
        // 得到其中的商品和数量信息
        for (OrderDetail detail : details) {
            skuMap.put(detail.getSkuId(), detail.getNum());
        }

        // 5.调用商品微服务，恢复库存，注意分布式事务问题
        try {
            itemClient.addStock(skuMap);
        } catch (FeignException e) {
            throw new LyException(e.status(), e.contentUTF8());
        }

    }

    private OrderDetail buildOrderDetailFromSku(SkuDTO sku, Long orderId,  Integer num) {
        OrderDetail detail = new OrderDetail();
        detail.setSkuId(sku.getId());
        detail.setPrice(sku.getPrice());
        detail.setOrderId(orderId);
        detail.setNum(num);
        detail.setImage(StringUtils.substringBefore(sku.getImages(), ","));
        detail.setSpec(sku.getSpecialSpec());
        detail.setTitle(sku.getTitle());
        return detail;
    }
}