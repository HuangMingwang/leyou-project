package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 5:33 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_brand")
@NoArgsConstructor
public class Brand extends BaseEntity {
    @TableId
    private Long id;

    private String name;

    private String image;

    private Character letter;
}
