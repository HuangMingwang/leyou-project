package com.leyou.search.mq;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.leyou.common.constants.BaseMQConstants.ExchangeConstants.ITEM_EXCHANGE_NAME;
import static com.leyou.common.constants.BaseMQConstants.QueueConstants.SEARCH_ITEM_DOWN;
import static com.leyou.common.constants.BaseMQConstants.QueueConstants.SEARCH_ITEM_UP;
import static com.leyou.common.constants.BaseMQConstants.RoutingKeyConstants.ITEM_DOWN_KEY;
import static com.leyou.common.constants.BaseMQConstants.RoutingKeyConstants.ITEM_UP_KEY;

/**
 * @author Huang Mingwang
 * @create 2021-05-31 11:54 上午
 */
@Component
public class ItemListener {

    private final SearchService searchService;

    public ItemListener(SearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SEARCH_ITEM_UP, durable = "true"),
            exchange = @Exchange(name = ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ITEM_UP_KEY
    ))
    public void listenItemUp(Long spuId){
        if(spuId != null){
            // 新增商品到索引库
            searchService.saveGoodsById(spuId);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SEARCH_ITEM_DOWN, durable = "true"),
            exchange = @Exchange(name = ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ITEM_DOWN_KEY
    ))
    public void listenItemDown(Long spuId){
        if(spuId != null){
            // 从索引库删除指定商品
            searchService.deleteGoodsById(spuId);
        }
    }
}
