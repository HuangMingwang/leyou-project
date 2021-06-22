package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.entity.SpecGroup;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 4:52 下午
 */

public interface SpecGroupService extends IService<SpecGroup> {
    List<SpecGroupDTO> querySpecGroupDetailByCategoryId(Long id);
}
