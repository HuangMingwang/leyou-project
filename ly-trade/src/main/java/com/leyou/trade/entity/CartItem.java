package com.leyou.trade.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 每个用户创建一个集合，作为这个用户的购物车，用户id作为集合名一部分。
 * spEL表达式 调用UserHolder的静态方法getUser()
 * @author Huang Mingwang
 * @create 2021-06-08 9:47 上午
 */
@Data
@Document("cart_user_#{T(com.leyou.trade.utils.UserHolder).getUser()}")
public class CartItem {
    @Id
    private Long skuId;// 商品id
    private String title;// 标题
    private String image;// 图片
    private Long price;// 加入购物车时的价格
    private Integer num;// 购买数量
    private String spec;// 商品规格参数
}
