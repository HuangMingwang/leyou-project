package com.leyou.trade.mq;

import com.leyou.common.constants.BaseMQConstants;
import com.leyou.trade.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Huang Mingwang
 * @create 2021-06-13 11:51 下午
 */
@Component
public class OrderMessageListener {

    private final OrderService orderService;

    public OrderMessageListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = BaseMQConstants.QueueConstants.EVICT_ORDER_QUEUE)
    public void listenOverdueOrder(Long orderId){
        if(orderId != null) {
            // 清理订单
            orderService.evictOrder(orderId);
        }
    }
}
