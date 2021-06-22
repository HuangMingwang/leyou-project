package com.leyou.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 12:07 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_order_detail")
public class OrderDetail extends BaseEntity {
    /**
     * 订单编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 订单编号
     */
    private Long orderId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 商品购买数量
     */
    private Integer num;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品单价
     */
    private Long price;
    /**
     * 商品规格数据
     */
    private String spec;
    /**
     * 图片
     */
    private String image;
}
