package com.leyou.search.web;


import com.leyou.search.dto.SearchParamDTO;
import com.leyou.search.entity.Goods;
import com.leyou.search.service.SearchService;
import com.leyou.starter.elastic.entity.PageInfo;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-05-31 11:43 上午
 */
@RestController
@RequestMapping("/goods")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 初始化索引库
     */
    @GetMapping("/initialization")
    public Mono<String> init() {
        // 创建索引库
        searchService.createIndexAndMapping();
        // 加载数据
        searchService.loadData();
        return Mono.just("导入成功");
    }

    /**
     * 自动补全
     *
     * @param key 用户输入的内容
     * @return 补全的词条列表
     */
    @GetMapping("/suggestion")
    public Mono<List<String>> getSuggestion(@RequestParam("key") String key) {
        return searchService.getSuggestion(key);
    }


    /**
     * 搜索商品
     * @param searchParamDTO 搜索条件
     * @return 商品列表和分页信息
     */
    @PostMapping("/list")
    public Mono<PageInfo<Goods>> searchGoods(@RequestBody SearchParamDTO searchParamDTO) {
        return searchService.searchGoods(searchParamDTO);
    }

    /**
     * 查询过滤项
     * @param searchParamDTO 查询条件
     * @return 过滤项
     */
    @PostMapping("/filter")
    public Mono<Map<String, List<?>>> getFilters(@RequestBody SearchParamDTO searchParamDTO) {

        return searchService.getFilters(searchParamDTO);
    }


}
