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
 * @create 2021-05-26 4:45 下午
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SpecParamDTO extends BaseDTO {
    private Long id;
    private Long categoryId;
    private Long groupId;
    private String name;
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
    private String options;
    /**
     * 规格参数值
     */
    private Object value;

    public SpecParamDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<SpecParamDTO> convertEntityList(Collection<T> list) {
        return list.stream().map(SpecParamDTO::new).collect(Collectors.toList());
    }
}
