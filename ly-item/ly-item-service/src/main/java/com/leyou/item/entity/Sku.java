package com.leyou.item.entity;

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
 * @create 2021-05-26 9:24 下午
 */
@Data
@TableName("tb_sku" )
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Sku extends BaseEntity {

    @TableField("id" )
    @TableId
    private Long id;
    @TableField("spu_id" )
    private Long spuId;
    @TableField("title" )
    private String title;
    @TableField("images" )
    private String images;
    @TableField("stock" )
    private Integer stock;
    @TableField("price" )
    private Long price;
    @TableField("indexes" )
    private String indexes;
    @TableField("sold" )
    private Long sold;
    @TableField("special_spec" )
    private String specialSpec;
    @TableField("saleable" )
    private Boolean saleable;

}