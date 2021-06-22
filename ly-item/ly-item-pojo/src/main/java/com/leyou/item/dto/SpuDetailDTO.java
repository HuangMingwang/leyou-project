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
 * @create 2021-05-28 9:31 下午
 */
@Data
@NoArgsConstructor(staticName = "of")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SpuDetailDTO extends BaseDTO {
    private Long spuId;

    private String description;

    private String packingList;

    private String afterService;
    // 规格参数
    private String specification;

    public SpuDetailDTO(BaseEntity entity) {
        super(entity);
    }

    public static <T extends BaseEntity> List<SpuDetailDTO> convertEntityList(Collection<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        return list.stream().map(SpuDetailDTO::new).collect(Collectors.toList());
    }
}
