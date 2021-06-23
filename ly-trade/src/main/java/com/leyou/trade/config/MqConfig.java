package com.leyou.trade.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import static com.leyou.common.constants.BaseMQConstants.ExchangeConstants.DEAD_EXCHANGE_NAME;
import static com.leyou.common.constants.BaseMQConstants.ExchangeConstants.ORDER_EXCHANGE_NAME;
import static com.leyou.common.constants.BaseMQConstants.QueueConstants.DEAD_ORDER_QUEUE;
import static com.leyou.common.constants.BaseMQConstants.QueueConstants.EVICT_ORDER_QUEUE;
import static com.leyou.common.constants.BaseMQConstants.RoutingKeyConstants.EVICT_ORDER_KEY;
/**
 * @author Huang Mingwang
 * @create 2021-06-13 11:19 下午
 */
@Configuration
public class MqConfig {
    // 30分钟
    private static final long ORDER_QUEUE_DELAY_TIME = 30000;
    /**
     * ly-order-exchange 普通任务交换机，将消息转发到死信队列
     */
    @Bean
    public TopicExchange orderExchange(){
        return new TopicExchange(ORDER_EXCHANGE_NAME, true, false);
    }
    /**
     * ly-dead-exchange 死信交换机，接收死信队列转过来的消息，并投递给任务队列
     */
    @Bean
    public TopicExchange deadExchange(){
        return new TopicExchange(DEAD_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue deadOrderQueue(){
        Map<String, Object> args = new HashMap<>(2);
        // x-message-ttl 声明队列TTL值
        args.put("x-message-ttl", ORDER_QUEUE_DELAY_TIME);
        // x-dead-letter-exchange 声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        return QueueBuilder.durable(DEAD_ORDER_QUEUE).withArguments(args).build();
    }

    @Bean
    public Queue evictOrderQueue(){
        return QueueBuilder.durable(EVICT_ORDER_QUEUE).build();
    }

    /**
     * 将死信队列与ly.order.exchange交换机绑定
     */
    @Bean
    public Binding bindingDeadQueue(){
        return BindingBuilder.bind(deadOrderQueue()).to(orderExchange()).with(EVICT_ORDER_KEY);
    }

    /**
     * 把死信交换机与ly.evict.order.queue绑定
     */
    @Bean
    public Binding bindingEvictQueue(){
        return BindingBuilder.bind(evictOrderQueue()).to(deadExchange()).with(EVICT_ORDER_KEY);
    }

    /**
     * JSON的消息转换器
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
