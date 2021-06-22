package com.leyou.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 通用的分页结果对象
 * @author Huang Mingwang
 * @create 2021-05-24 5:47 下午
 */
@Data
public class PageDTO<T> {
    // 总条数
    private Long total;
    // 总页数
    private Long totalPage;
    // 当前页数据
    private List<T> items;

    public PageDTO() {
    }

    public PageDTO(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageDTO(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
