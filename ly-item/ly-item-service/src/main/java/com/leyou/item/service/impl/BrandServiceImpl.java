package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.constants.BaseRedisConstants;
import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryBrandService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 5:43 下午
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    private final StringRedisTemplate stringRedisTemplate;
    private final CategoryBrandService categoryBrandService;

    public BrandServiceImpl(StringRedisTemplate stringRedisTemplate, CategoryBrandService categoryBrandService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.categoryBrandService = categoryBrandService;
    }

    @Override
    public PageDTO<BrandDTO> queryBrandByPage(String key, Integer page, Integer rows) {
        // 1.分页信息健壮性处理
        page = Math.min(page, 100);
        rows = Math.min(rows, 5);
        // 2. 分页
        Page<Brand> info = new Page<>(page, rows);
        // 3.判断key是否存在
        boolean isKeyExists = StringUtils.isNoneBlank(key);
        // 4.如果key存在目， 添加like和eq的查询条件，否则不添加
        query().like(isKeyExists, "name", key)
                .or()
                .eq(isKeyExists, "letter",key)
                .page(info);
        // 5.封装结果
        List<Brand> list = info.getRecords();
        return new PageDTO<>(info.getTotal(), info.getPages(), BrandDTO.convertEntityList(list));
    }

    @Override
    public List<Brand> queryBrandByCategoryId(Long id) {
        // 通过中间表categorybrand建立联系
        return listByIds(categoryBrandService.query().eq("category_id", id)
        .list().stream().map(CategoryBrand::getBrandId).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void saveBrand(BrandDTO brandDTO) {
        // 1.将品牌插入，并返回brandId值
        Brand brand = brandDTO.toEntity(Brand.class);
        boolean flag = save(brand);
        if (!flag) {
            throw new LyException(500, "新增品牌失败");
        }

        /* // 2.获取分类id， 包含手机、空调、洗衣机
        List<Long> categoryIds = brandDTO.getCategoryIds();
        // 3.封装中间表对象集合
        Long brandId = brand.getId();// 小米

        List<CategoryBrand> categoryBrands = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            // 创建中间表对象
            CategoryBrand categoryBrand = CategoryBrand.of(categoryId, brandId);
            categoryBrands.add(categoryBrand);
        }*/

        // 2. 将brandId值和categoryIds逐条插入中间表
        flag = categoryBrandService.saveBatch(brandDTO.getCategoryIds().stream()
                .map(id -> CategoryBrand.of(id, brand.getId())).collect(Collectors.toList()));
        if (!flag) {
            throw new LyException(500, "新增品牌分类中间表失败");
        }
        stringRedisTemplate.opsForSet().add(BaseRedisConstants.BRAND_PIC_DB_RESOURCE, brand.getImage() );
    }

    @Override
    @Transactional
    public void updateBrand(BrandDTO brandDTO) {
        // 1.根据brandId更新brand
        Brand brand = brandDTO.toEntity(Brand.class);
        String image = getById(brand.getId()).getImage();
        boolean flag = updateById(brand);
        if (!flag) {
            throw new LyException(500, "更新品牌异常");
        }
        // 2.根据brandId删除中间表对应表项
        flag = categoryBrandService.remove(new QueryWrapper<CategoryBrand>().eq("brand_id", brand.getId()));
        if (!flag) {
            throw new LyException(500, "根据品牌id删除中间表失败");
        }

        flag = categoryBrandService.saveBatch(brandDTO.getCategoryIds().stream()
                .map(id -> CategoryBrand.of(id, brand.getId())).collect(Collectors.toList()));
        if (!flag) {
            throw new LyException(500, "根据品牌id更新中间表失败");
        }

        stringRedisTemplate.opsForSet().add(BaseRedisConstants.BRAND_PIC_DB_RESOURCE, brand.getImage());
        stringRedisTemplate.opsForSet().remove(BaseRedisConstants.BRAND_PIC_DB_RESOURCE, image);
    }

    @Override
    @Transactional
    public void removeByBrandId(Long id) {
        Brand brand = getById(id);
        String image = brand.getImage();
        // 删除中间表
        boolean flag = categoryBrandService.remove(new QueryWrapper<CategoryBrand>().eq("brand_id", id));
        if (!flag) {
            throw new LyException(500, "根据品牌id删除中间表失败");
        }
        // 删除品牌表
        flag = removeById(id);
        if (!flag) {
            throw new LyException(500, "根据id删除品牌失败");
        }
        stringRedisTemplate.opsForSet().remove(BaseRedisConstants.BRAND_PIC_DB_RESOURCE,image);
    }
}
