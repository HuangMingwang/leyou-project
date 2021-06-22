package com.leyou.item.dto;

import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:28 下午
 */
@Data
@NoArgsConstructor(staticName = "of")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SkuDTO extends BaseDTO {
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    /**
     * 商品特殊规格的键值对
     */
    private String specialSpec;
    /**
     * 商品特殊规格的下标
     */
    private String indexes;
    /**
     * 是否有效，逻辑删除用
     */
    private Boolean saleable;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 销量
     */
    private Long sold;

    public SkuDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<SkuDTO> convertEntityList(Collection<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream().map(SkuDTO::new).collect(Collectors.toList());
    }
}
