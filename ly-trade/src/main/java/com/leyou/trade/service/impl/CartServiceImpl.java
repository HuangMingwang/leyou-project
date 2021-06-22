package com.leyou.trade.service.impl;

import com.leyou.common.exception.LyException;
import com.leyou.trade.entity.CartItem;
import com.leyou.trade.repository.CartRepository;
import com.leyou.trade.service.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 10:50 上午
 */
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void addCartItem(CartItem cartItem) {
        // 判断购物车商品是否存在，要修改还是新增
        cartItem = saveOrUpdateCartItem(cartItem);
        // 写入mongoDB
        cartRepository.save(cartItem);
    }

    private CartItem saveOrUpdateCartItem(CartItem cartItem) {
        // 尝试获取购物车中与要添加的商品id一致的商品
        Optional<CartItem> optionalCartItem = cartRepository.findById(cartItem.getSkuId());
        // 判断是否存在
        if (optionalCartItem.isPresent()) {
            // 保留新增的数量
            Integer num = cartItem.getNum();
            // 存在，取出之前的购物车数据
            cartItem = optionalCartItem.get();
            // 数量累加
            cartItem.setNum(cartItem.getNum() + num);
        }
        return cartItem;
    }

    @Override
    public List<CartItem> queryCartList() {
        return cartRepository.findAll();
    }

    @Override
    public void updateCartItemNum(Long skuId, Integer num) {
        // 查询购物车
        Optional<CartItem> optionalCartItem = cartRepository.findById(skuId);
        if (!optionalCartItem.isPresent()) {
            throw new LyException(400, "购物车商品不存在");
        }
        // 修改数量
        CartItem cartItem = optionalCartItem.get();
        cartItem.setNum(num);
        cartRepository.save(cartItem);
    }

    @Override
    public void addCartItemList(List<CartItem> cartItems) {
        // 判断新增或修改
        List<CartItem> saveOrUpdateList = new ArrayList<>(cartItems.size());
        for (CartItem cartItem :
                cartItems) {
            saveOrUpdateList.add(saveOrUpdateCartItem(cartItem));
        }
        // 批量新增
        cartRepository.saveAll(saveOrUpdateList);
    }


}
