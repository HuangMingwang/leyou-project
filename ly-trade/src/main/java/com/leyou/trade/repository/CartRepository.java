package com.leyou.trade.repository;

import com.leyou.trade.entity.CartItem;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 10:15 上午
 */

public interface CartRepository extends MongoRepository<CartItem, Long> {
}
