package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.entity.Sku;

import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:37 下午
 */
public interface SkuService extends IService<Sku> {
    void deductStock(Map<Long, Integer> cartMap);

    void addStock(Map<Long, Integer> skuMap);
}
