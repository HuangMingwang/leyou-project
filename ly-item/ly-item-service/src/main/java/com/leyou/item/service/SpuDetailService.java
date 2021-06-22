package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpuDetail;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:39 下午
 */
public interface SpuDetailService extends IService<SpuDetail> {

    List<SpecParamDTO> querySpecValues(Long id, Boolean searching);
}
