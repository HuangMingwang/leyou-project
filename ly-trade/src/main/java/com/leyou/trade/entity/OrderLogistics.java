package com.leyou.trade.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 2:02 下午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_order_logistics")
public class OrderLogistics extends BaseEntity {
    /**
     * 订单id，与订单表一对一
     */
    @TableId
    private Long orderId;
    /**
     * 物流单号
     */
    private String logisticsNumber;
    /**
     * 物流名称
     */
    private String logisticsCompany;
    /**
     * 收件人
     */
    private String addressee;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 街道
     */
    private String street;
    /**
     * 邮编
     */
    private String postcode;
}