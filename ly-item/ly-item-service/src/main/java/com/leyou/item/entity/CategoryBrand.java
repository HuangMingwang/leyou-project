package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Huang Mingwang
 * @create 2021-05-22 4:12 下午
 */
@Data
@NoArgsConstructor(staticName = "of")//会生成一个of()的静态方法，并把构造方法设置为私有的
@AllArgsConstructor(staticName = "of")
@TableName("tb_category_brand")
public class CategoryBrand {
    /**
     * IdType.INPUT 代表主键采用自己填写而不是自增长
     */
    @TableId(type = IdType.INPUT)
    private Long categoryId;
    @TableId(type = IdType.INPUT)
    private Long brandId;

}
