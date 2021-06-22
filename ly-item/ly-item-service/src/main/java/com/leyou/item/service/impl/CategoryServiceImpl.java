package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.CategoryBrandService;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-06-18 2:42 下午
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryBrandService categoryBrandService;
    @Override
    public List<CategoryDTO> queryCategoryByBrandId(Long id) {
        // 根据品牌id，查询中间表，得到中间表对象集合
        QueryWrapper<CategoryBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id", id);
        List<CategoryBrand> categoryBrandList = categoryBrandService.list(queryWrapper);
        if (CollectionUtils.isEmpty(categoryBrandList)) {
            return Collections.emptyList();
        }
        // 获取分类id集合
        List<Long> categoryIds = categoryBrandList.stream().map(CategoryBrand::getCategoryId).collect(Collectors.toList());
        // 根据分类id集合，查询分类对象集合
        List<Category> categories = listByIds(categoryIds);
        // 转化为DTO
        return CategoryDTO.convertEntityList(categories);
    }
}
