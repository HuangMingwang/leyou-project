package com.leyou.page.service.impl;

import com.leyou.common.utils.BeanHelper;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.*;
import com.leyou.page.dto.SpecGroupNameDTO;
import com.leyou.page.dto.SpecParamNameDTO;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-06-01 2:52 下午
 */
@Service
public class PageServiceImpl implements PageService {


    @Autowired
    private ItemClient itemClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX_SPU = "page:spu:id:";
    private static final String KEY_PREFIX_SKU = "page:sku:id:";
    private static final String KEY_PREFIX_DETAIL = "page:detail:id:";
    private static final String KEY_PREFIX_CATEGORY = "page:category:id:";
    private static final String KEY_PREFIX_BRAND = "page:brand:id:";
    private static final String KEY_PREFIX_SPEC = "page:spec:id:";

    @Override
    public String loadSpuData(Long spuId) {
        SpuDTO spu = itemClient.querySpuById(spuId);
        // 组织数据
        Map<String, Object> map = new HashMap<>();
        map.put("id", spu.getId());
        map.put("name", spu.getName());
        map.put("categoryIds", spu.getCategoryIds());
        map.put("brandId", spu.getBrandId());
        String json = JsonUtils.toJson(map);
        redisTemplate.opsForValue().set(KEY_PREFIX_SPU + spuId, json);
        return json;
    }

    @Override
    public String loadSpuDetailData(Long spuId) {
        SpuDetailDTO spuDetailDTO = itemClient.querySpuDetailById(spuId);
        String json = JsonUtils.toJson(spuDetailDTO);
        redisTemplate.opsForValue().set(KEY_PREFIX_DETAIL + spuId, json);
        return json;
    }

    @Override
    public String loadSkuListData(Long spuId) {
        // 查询信息
        List<SkuDTO> skuList = itemClient.querySkuBySpuId(spuId);
        String json = JsonUtils.toJson(skuList);
        // 存入redis
        redisTemplate.opsForValue().set(KEY_PREFIX_SKU + spuId, json);
        return json;
    }

    @Override
    public String loadCategoriesData(List<Long> ids) {
        List<CategoryDTO> list = itemClient.queryCategoryByIds(ids);
        List<HashMap<String, Object>> rList = list.stream().map(categoryDTO -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", categoryDTO.getId());
            map.put("name", categoryDTO.getName());
            return map;
        }).collect(Collectors.toList());
        String json = JsonUtils.toJson(rList);
        redisTemplate.opsForValue().set(KEY_PREFIX_CATEGORY + ids.get(ids.size() - 1), json);
        return json;
    }

    @Override
    public String loadBrandData(Long brandId) {
// 查询信息
        BrandDTO brand = itemClient.queryBrandById(brandId);
        Map<String, Object> map = new HashMap<>();
        map.put("id", brand.getId());
        map.put("name", brand.getName());
        String json = JsonUtils.toJson(map);
        // 存入Redis
        redisTemplate.opsForValue().set(KEY_PREFIX_BRAND + brandId, json);
        return json;

    }

    @Override
    public String loadSpecData(Long categoryId) {
        List<SpecGroupDTO> list = itemClient.querySpecGroupDetailByCategoryId(categoryId);
        List<SpecGroupNameDTO> collect = list.stream().map(specGroupDTO -> {
            SpecGroupNameDTO groupNameDTO = new SpecGroupNameDTO();
            groupNameDTO.setName(specGroupDTO.getName());
            groupNameDTO.setParams(BeanHelper.copyWithCollection(specGroupDTO.getParams(), SpecParamNameDTO.class));
            return groupNameDTO;
        }).collect(Collectors.toList());
        String json = JsonUtils.toJson(collect);
        redisTemplate.opsForValue().set(KEY_PREFIX_SPEC + categoryId, json);
        return json;
    }

    @Override
    public Boolean deleteSku(Long spuId) {
        return redisTemplate.delete(KEY_PREFIX_SKU + spuId);
    }
}
