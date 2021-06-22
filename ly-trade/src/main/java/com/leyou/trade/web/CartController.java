package com.leyou.trade.web;

import com.leyou.trade.entity.CartItem;
import com.leyou.trade.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 10:55 上午
 */
@RestController
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * 添加购物车
     * @param cartItem 购物车条目
     * @return 无
     */
    @PostMapping
    public ResponseEntity<Void> addCartItem(@RequestBody CartItem cartItem){
        cartService.addCartItem(cartItem);
        // 添加成功，返回201
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车
     * @return 购物车列表
     */
    @GetMapping("list")
    public ResponseEntity<List<CartItem>> queryCartList(){
        return ResponseEntity.ok(cartService.queryCartList());
    }

    /**
     * 修改购物车商品数量
     * @param skuId 商品id
     * @param num 数量
     * @return 无
     */
    @PutMapping
    public ResponseEntity<Void> updateCartItemNum(@RequestParam("id") Long skuId, @RequestParam("num") Integer num){
        cartService.updateCartItemNum(skuId, num);
        return ResponseEntity.noContent().build();
    }

    /**
     * 批量添加购物车商品
     * @param cartItems 要添加的购物车列表
     * @return 无
     */
    @PostMapping("list")
    public ResponseEntity<Void> addCartItemList(@RequestBody List<CartItem> cartItems){
        cartService.addCartItemList(cartItems);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}