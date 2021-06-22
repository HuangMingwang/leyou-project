package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 5:38 下午
 */
public interface BrandService extends IService<Brand> {
    PageDTO<BrandDTO> queryBrandByPage(String key, Integer page, Integer rows);

    List<Brand> queryBrandByCategoryId(Long id);

    void saveBrand(BrandDTO brandDTO);

    void updateBrand(BrandDTO brandDTO);

    void removeByBrandId(Long id);
}
