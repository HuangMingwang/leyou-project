package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 4:39 下午
 */
@TableName("tb_spec_param")
@Data
@EqualsAndHashCode(callSuper = false)
public class SpecParam extends BaseEntity {
    @TableId
    private Long id;
    private Long categoryId;
    private Long groupId;
    @TableField("`name`")
    private String name;
    @TableField("`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
    @TableField("`options`")
    private String options;
}
