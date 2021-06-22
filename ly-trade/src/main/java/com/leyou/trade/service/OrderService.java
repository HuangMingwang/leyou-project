package com.leyou.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.trade.dto.OrderFormDTO;
import com.leyou.trade.entity.Order;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 2:13 下午
 */
public interface OrderService extends IService<Order> {
    Long createOrder(OrderFormDTO orderFormDTO);

    String getPayUrl(Long id);

    void handleNotify(Map<String, String> data);

    Integer queryOrderState(Long orderId);

    void evictOrder(Long orderId);
}
