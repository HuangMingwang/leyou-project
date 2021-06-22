package com.leyou.page.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-06-01 2:51 下午
 */
@Data
public class SpecGroupNameDTO {
    private String name;
    private List<SpecParamNameDTO> params;
}