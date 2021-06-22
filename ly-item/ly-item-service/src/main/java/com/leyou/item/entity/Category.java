package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Huang Mingwang
 * @create 2021-05-22 2:33 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)   //是否将父类的内容用于hash运算
@TableName("tb_category")
@NoArgsConstructor
public class Category extends BaseEntity {
    @TableId
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}
