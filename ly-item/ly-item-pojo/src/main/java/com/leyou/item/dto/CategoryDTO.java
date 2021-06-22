package com.leyou.item.dto;

import com.leyou.common.dto.BaseDTO;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-05-22 2:54 下午
 */
@Data
@NoArgsConstructor(staticName = "of")
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends BaseDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;

    public CategoryDTO(BaseEntity entity) {
        super(entity);
    }

    /**
     * 将PO集合转为DTO集合
     *
     * @param list PO对象的集合
     * @param <T>  PO的类型
     * @return DTO集合
     */
    public static <T extends BaseEntity> List<CategoryDTO> convertEntityList(Collection<T> list) {
        if (list == null) {
            return null;
        }
        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

}
