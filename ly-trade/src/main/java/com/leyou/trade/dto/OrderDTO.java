package com.leyou.trade.dto;

import com.leyou.common.dto.BaseDTO;
import com.leyou.trade.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 2:24 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDTO extends BaseDTO {
    private Long orderId;
    private Long totalFee;
    private Long postFee;
    private Long actualFee;
    private Integer paymentType;
    private Long userId;
    private Integer status;
    private Date createTime;
    private Date payTime;
    private Date consignTime;
    private Date endTime;
    private Date closeTime;
    private Date commentTime;

    public OrderDTO(Order entity) {
        super(entity);
        this.status = entity.getStatus().getValue();
    }
}