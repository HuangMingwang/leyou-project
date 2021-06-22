package com.leyou.search.service;

import com.leyou.search.dto.SearchParamDTO;
import com.leyou.search.entity.Goods;
import com.leyou.starter.elastic.entity.PageInfo;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-05-31 10:15 上午
 */
public interface SearchService {
    /**
     * 创建索引库并设置映射
     */
    void createIndexAndMapping();

    /**
     * 加载数据到索引库
     */
    void loadData();

    /**
     * 自动补全
     * @param key 关键字
     * @return Mono
     */
    Mono<List<String>> getSuggestion(String key);

    /**
     * 搜索
     * @param searchParamDTO DTO
     * @return Mono
     */
    Mono<PageInfo<Goods>> searchGoods(SearchParamDTO searchParamDTO);

    /**
     * 查询过滤项
     * @param searchParamDTO DTO
     * @return Mono<Map<String, List<?>>>
     */
    Mono<Map<String, List<?>>> getFilters(SearchParamDTO searchParamDTO);

    /**
     * 新增一个商品到索引库
     * @param id 商品id
     */
    void saveGoodsById(Long id);

    /**
     * 从索引库删除一个商品
     * @param id 商品id
     */
    void deleteGoodsById(Long id);
}
