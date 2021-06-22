package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.entity.Spu;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:38 下午
 */
public interface SpuService extends IService<Spu> {

    PageDTO<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, Long categoryId, Long brandId, Long id);

    void saveGoods(SpuDTO spuDTO);

    void updateSaleable(Long id, Boolean saleable);

    SpuDTO querySpuDetailAndSkuById(Long id);

    void updateGoods(SpuDTO spuDTO);
}

