package com.leyou.trade.service;

import com.leyou.trade.entity.CartItem;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 10:50 上午
 */
public interface CartService {
    void addCartItem(CartItem cartItem);

    List<CartItem> queryCartList();

    void updateCartItemNum(Long skuId, Integer num);

    void addCartItemList(List<CartItem> cartItems);
}
