package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.constants.BaseMQConstants;
import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.*;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:39 下午
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final SkuService skuService;
    private final SpuDetailService spuDetailService;
    private final RabbitTemplate rabbitTemplate;

    public SpuServiceImpl(BrandService brandService, CategoryService categoryService, SkuService skuService, SpuDetailService spuDetailService, RabbitTemplate rabbitTemplate) {
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.skuService = skuService;
        this.spuDetailService = spuDetailService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public PageDTO<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, Long categoryId, Long brandId, Long id) {
        //健壮性
        page = Math.max(1, Math.min(page, 100));
        rows = Math.max(5, rows);
        // 查询条件
        Spu spu = new Spu();
        spu.setBrandId(brandId);
        spu.setCid3(categoryId);
        spu.setId(id);
        spu.setSaleable(saleable);

        QueryWrapper<Spu> wrapper = new QueryWrapper<>(spu);
        // 查询数据
        IPage<Spu> spuPage = page(new Page<Spu>(page, rows), wrapper);
        // 处理结果
        List<Spu> records = spuPage.getRecords();
        if (CollectionUtils.isEmpty(records)){
            throw  new LyException(400, "商品不存在");
        }
        // DTO转换
        List<SpuDTO> spuDTOS = SpuDTO.convertEntityList(records);
        spuDTOS.forEach(spuDTO -> handleCategoryNameAndBrandName(spuDTO));

        PageDTO<SpuDTO> pageDTO = new PageDTO<>(spuPage.getTotal(), spuPage.getPages(), spuDTOS);
        return pageDTO;
    }

    void handleCategoryNameAndBrandName(SpuDTO spuDTO){
        // 处理品牌名称
        Brand brand = brandService.getById(spuDTO.getBrandId());
        if (brand != null) {
            spuDTO.setBrandName(brand.getName());
        }
        // 处理分类名称
        Collection<Category> categories = categoryService.listByIds(spuDTO.getCategoryIds());
        if (!CollectionUtils.isEmpty(categories)) {
            spuDTO.setCategoryName(categories.stream().map(Category::getName).collect(Collectors.joining("/")));
        }
    }

    @Override
    @Transactional
    public void saveGoods(SpuDTO spuDTO) {
        /**
         * 1.获取sku和spudetail表
         * 2.保存他们
         */
        SpuDetail spuDetail = spuDTO.getSpuDetail().toEntity(SpuDetail.class);
        List<Sku> skuList = spuDTO.getSkus().stream().map(skuDTO -> skuDTO.toEntity(Sku.class)).collect(Collectors.toList());
        Spu spu = spuDTO.toEntity(Spu.class);

        spu.setSaleable(false);
        boolean flag = save(spu);
        if (!flag) {
            throw new LyException(500, "保存商品spu失败");
        }

        spuDetail.setSpuId(spu.getId());
        flag = spuDetailService.save(spuDetail);
        if (!flag) {
            throw new LyException(500, "保存商品spudetail失败");
        }

        skuList.forEach(sku -> {sku.setSaleable(false); sku.setSpuId(spu.getId());});
        flag = skuService.saveBatch(skuList);
        if (!flag) {
            throw new LyException(500, "保存商品sku失败");
        }
    }

    @Override
    @Transactional
    public void updateSaleable(Long id, Boolean saleable) {
        // 1.更新SPU
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(saleable);
        boolean flag = updateById(spu);
        if (!flag) {
            throw new LyException(500, "更新上下架信息失败");
        }
        // 2.更新SKU
        flag = skuService.update().eq("spu_id", id).set("saleable", saleable).update();
        if (!flag) {
            throw new LyException(500, "更新sku上下架信息失败");
        }

        // 3.发送MQ消息
        String routingKey = saleable ? BaseMQConstants.RoutingKeyConstants.ITEM_UP_KEY
                : BaseMQConstants.RoutingKeyConstants.ITEM_DOWN_KEY;
        rabbitTemplate.convertAndSend(BaseMQConstants.ExchangeConstants.ITEM_EXCHANGE_NAME, routingKey, id);
    }
    /**
     * 1.查询spu
     * 2.根据spuid查询spudetail
     * 3.根据spuid查询skus
     * 4.封装分类名称和品牌名称
     */
    @Override
    public SpuDTO querySpuDetailAndSkuById(Long id) {

        SpuDTO spuDTO = new SpuDTO(getById(id));
        spuDTO.setSpuDetail(new SpuDetailDTO(spuDetailService.getOne(new QueryWrapper<SpuDetail>().
                eq("spu_id", id))));
        spuDTO.setSkus(SkuDTO.convertEntityList(skuService.lambdaQuery().eq(Sku::getSpuId, id).list()));
        handleCategoryNameAndBrandName(spuDTO);
        return spuDTO;
    }

    @Transactional
    @Override
    public void updateGoods(SpuDTO spuDTO) {
        // 1.修改spu
        // 1.1.判断是否包含spu信息
        if (spuDTO.getId() != null) {
            // 1.2.如果有，则修改
            Spu spu = spuDTO.toEntity(Spu.class);
            boolean success = updateById(spu);
            if(!success){
                throw new LyException(500, "更新商品失败！");
            }
        }
        // 2.修改Detail
        // 2.1.判断是否包含spuDetail信息
        if (spuDTO.getSpuDetail() != null) {
            // 2.2.如果有，则修改
            SpuDetail detail = spuDTO.getSpuDetail().toEntity(SpuDetail.class);
            boolean success = spuDetailService.updateById(detail);
            if(!success){
                throw new LyException(500, "更新商品失败！");
            }
        }


        // 3.修改Sku
        List<SkuDTO> skuDTOList = spuDTO.getSkus();
        if(CollectionUtils.isEmpty(skuDTOList)){
            // 没有，直接结束
            return;
        }
        // 3.1.判断是否包含saleable字段，包含说明需要删除
        // 目的是把要删除的sku，和新增修改的sku分离
        Map<Boolean, List<Sku>> map = skuDTOList.stream() // SkuDTO的流
                .map(skuDTO -> skuDTO.toEntity(Sku.class)) // SkuDTO -- > Sku的流
                .collect(Collectors.groupingBy(sku -> sku.getSaleable() == null));

        // 3.2.判断是否有需要删除的，如果有则删除
        List<Sku> deleteSkuList = map.get(false);
        // 3.2.1.判断是否为空
        if(!CollectionUtils.isEmpty(deleteSkuList)){
            // 3.2.2.删除，获取sku的id集合
            List<Long> idList = deleteSkuList.stream().map(Sku::getId).collect(Collectors.toList());
            // 3.2.3.批量删除
            skuService.removeByIds(idList);
        }

        // 3.3.判断是否有需要新增的，如果有新增
        List<Sku> insertOrUpdateSkuList = map.get(true);
        // 3.3.1.判断是否为空
        if(!CollectionUtils.isEmpty(insertOrUpdateSkuList)){
            // 3.3.2.批量新增或修改
            skuService.saveOrUpdateBatch(insertOrUpdateSkuList);
        }
    }
}
