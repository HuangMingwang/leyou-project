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
 * @create 2021-05-26 4:42 下午
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SpecGroupDTO extends BaseDTO {
    private Long id;
    private Long categoryId;
    private String name;

    private List<SpecParamDTO> params;

    public SpecGroupDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<SpecGroupDTO> convertEntityList(Collection<T> list) {
        return list.stream().map(SpecGroupDTO::new).collect(Collectors.toList());
    }
}
