package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.Category;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-06-18 2:40 下午
 */
public interface CategoryService extends IService<Category> {

    List<CategoryDTO> queryCategoryByBrandId(Long id);
}
