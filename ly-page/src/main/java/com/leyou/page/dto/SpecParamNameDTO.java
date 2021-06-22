package com.leyou.page.dto;

import lombok.Data;

/**
 * @author Huang Mingwang
 * @create 2021-06-01 2:48 下午
 */
@Data
public class SpecParamNameDTO {
    private Long id;
    private String name;
    private Boolean numeric;
    private Boolean generic;
    private String unit;
}
