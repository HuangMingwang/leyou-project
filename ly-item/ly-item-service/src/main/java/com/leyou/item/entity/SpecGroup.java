package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 4:38 下午
 */
@TableName("tb_spec_group")
@Data
@EqualsAndHashCode(callSuper = false)
public class SpecGroup extends BaseEntity {
    @TableId
    private Long id;
    private Long categoryId;
    private String name;
}
