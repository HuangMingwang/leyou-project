package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 9:20 下午
 */
@Data
@TableName("tb_spu_detail")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SpuDetail extends BaseEntity {
    @TableField("spu_id" )
    @TableId(type = IdType.INPUT)
    private Long spuId;
    @TableField("description" )
    private String description;
    /**
     * 规格参数值
     */
    @TableField("specification" )
    private String specification;
    /**
     * 包装清单
     */
    @TableField("packing_list" )
    private String packingList;
    /**
     * 售后服务
     */
    @TableField("after_service" )
    private String afterService;
}
