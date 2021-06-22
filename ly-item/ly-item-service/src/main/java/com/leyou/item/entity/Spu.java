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
 * @create 2021-05-26 9:19 下午
 */
@Data
@TableName("tb_spu")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Spu extends BaseEntity {
    @TableId
    @TableField("id" )
    private Long id;
    @TableField("`name`" )
    private String name;
    @TableField("title" )
    private String title;
    @TableField("cid1" )
    private Long cid1;
    @TableField("cid2" )
    private Long cid2;
    @TableField("cid3" )
    private Long cid3;
    @TableField("brand_id" )
    private Long brandId;
    @TableField("saleable" )
    private Boolean saleable;
}
