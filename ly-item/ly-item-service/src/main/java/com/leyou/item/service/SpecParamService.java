package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecParam;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 4:53 下午
 */
public interface SpecParamService extends IService<SpecParam> {
    List<SpecParamDTO> querySpecParams(Long cid, Long gid, Boolean searching);

}
