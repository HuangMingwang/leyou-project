package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 4:57 下午
 */
@Service
public class SpecGroupServiceImpl extends ServiceImpl<SpecGroupMapper, SpecGroup> implements SpecGroupService {
    private final SpecParamService specParamService;

    public SpecGroupServiceImpl(SpecParamService specParamService) {
        this.specParamService = specParamService;
    }

    @Override
    public List<SpecGroupDTO> querySpecGroupDetailByCategoryId(Long id) {
        // 根据id查找规格组
        List<SpecGroup> specGroups = query().eq("category_id", id).list();
        List<SpecGroupDTO> groupDTOList = SpecGroupDTO.convertEntityList(specGroups);
        if (CollectionUtils.isEmpty(groupDTOList)) {
            throw new LyException(404, "该分类下的规格组不存在");
        }
        // 查询规格参数
        List<SpecParam> specParamList = specParamService.query().eq("category_id", id).list();
        List<SpecParamDTO> paramDTOList = SpecParamDTO.convertEntityList(specParamList);
        Map<Long, List<SpecParamDTO>> listMap = paramDTOList.stream().collect(Collectors.groupingBy(SpecParamDTO::getGroupId));
        groupDTOList.forEach(group->group.setParams(listMap.get(group.getId())));
        return groupDTOList;



    }
}
