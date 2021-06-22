package com.leyou.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.MybatisEnumTypeHandler;
import com.leyou.common.entity.BaseEntity;
import com.leyou.trade.entity.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 12:04 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_order")
public class Order extends BaseEntity {
    /**
     * 订单编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long orderId;
    /**
     * 商品金额
     */
    private Long totalFee;
    /**
     * 邮费
     */
    private Long postFee;
    /**
     * 实付金额
     */
    private Long actualFee;
    /**
     * 付款方式：1:微信支付, 2:支付宝支付
     */
    private Integer paymentType;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单状态,1、未付款 2、已付款,未发货 3、已发货,未确认 4、确认收货，交易成功 5、交易取消，订单关闭 6、交易结束
     */
    @TableField(typeHandler = MybatisEnumTypeHandler.class)
    private OrderStatus status;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 发货时间
     */
    private Date consignTime;
    /**
     * 确认收货时间
     */
    private Date endTime;
    /**
     * 交易关闭时间
     */
    private Date closeTime;
    /**
     * 评价时间
     */
    private Date commentTime;
}
