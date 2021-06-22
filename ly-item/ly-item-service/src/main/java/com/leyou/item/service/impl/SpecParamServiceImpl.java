package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.service.SpecParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 4:55 下午
 */
@Service
public class SpecParamServiceImpl extends ServiceImpl<SpecParamMapper, SpecParam> implements SpecParamService {
    @Override
    public List<SpecParamDTO> querySpecParams(Long categoryId, Long groupId, Boolean searching) {
        //健壮性判断
        if (categoryId == null && groupId == null){
            throw new LyException(400, "请求参数不能为空");
            //400为无效的请求,401位未授权,403为授权了但权限不够
        }
        List<SpecParam> list = query().eq(categoryId != null, "category_id", categoryId)
                .eq(groupId != null, "group_id", groupId)
                .eq(searching != null, "searching", searching)
                .list();

        return SpecParamDTO.convertEntityList(list);
    }
}
