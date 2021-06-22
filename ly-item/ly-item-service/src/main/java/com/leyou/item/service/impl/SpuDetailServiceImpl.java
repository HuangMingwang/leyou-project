package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.Spu;
import com.leyou.item.entity.SpuDetail;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.service.SpecParamService;
import com.leyou.item.service.SpuDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:41 下午
 */
@Service
public class SpuDetailServiceImpl extends ServiceImpl<SpuDetailMapper, SpuDetail> implements SpuDetailService {
    private final SpuMapper spuMapper;
    private final SpecParamService specParamService;

    public SpuDetailServiceImpl(SpuMapper spuMapper, SpecParamService specParamService) {
        this.spuMapper = spuMapper;
        this.specParamService = specParamService;
    }

    @Override
    public List<SpecParamDTO> querySpecValues(Long id, Boolean searching) {
        // 1.查询spu，获取其中的商品分类id
        Spu spu = spuMapper.selectById(id);
        if (spu == null) {
            throw new LyException(404, "商品不存在！");
        }
        // 2.查询规格参数，根据商品分类id
        List<SpecParamDTO> params = specParamService.querySpecParams(spu.getCid3(), null, searching);
        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(404, "商品规格不存在！");
        }

        // 3.查询商品详情，
        SpuDetail spuDetail = getById(id);
        // 4.获取其中的规格参数值
        String specification = spuDetail.getSpecification();
        Map<Long, Object> specMap = JsonUtils.toMap(specification, Long.class, Object.class);

        // 5.把规格参数key和规格参数value配对
        for (SpecParamDTO param : params) {
            // 根据SpecParam的id到specMap中查找对应的值
            param.setValue(specMap.get(param.getId()));
        }
        return params;
    }
}
