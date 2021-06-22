package com.leyou.item.controller;

import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.Sku;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:44 下午
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    private final SpuService spuService;
    private final SpuDetailService spuDetailService;
    private final SkuService skuService;

    public GoodsController(SpuService spuService, SpuDetailService spuDetailService, SkuService skuService) {
        this.spuService = spuService;
        this.spuDetailService = spuDetailService;
        this.skuService = skuService;
    }

    /**
     * 分页查询spu
     *
     * @param brandId    品牌id
     * @param categoryId 分类id
     * @param id         spu的id
     * @param page       当前页
     * @param rows       每页大小
     * @param saleable   上架商品或下架商品
     * @return 当前页商品数据
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageDTO<SpuDTO>>  querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "id", required = false) Long id) {
        PageDTO<SpuDTO> pageDTO = spuService.querySpuByPage(page, rows, saleable, categoryId, brandId, id);
        return ResponseEntity.ok(pageDTO);
    }

    /**
     * 新增商品
     *
     * @param spuDTO spuDTO
     * @return 无
     */
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDTO spuDTO) {
        spuService.saveGoods(spuDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改商品上下架
     * @param id 商品spu的id
     * @param saleable true为上架，false为下架
     * @return 无
     */
    @PutMapping("/saleable")
    public ResponseEntity<Void> updateSaleable(@RequestParam("id") Long id,
                                               @RequestParam("saleable") Boolean saleable) {
        spuService.updateSaleable(id, saleable);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据id查询spu，sku，spuDetail等信息
     * @param id 商品id
     * @return 商品信息SpuDTO
     */
    @GetMapping("{id}")
    public ResponseEntity<SpuDTO> querySpuDetailAndSkuById(@PathVariable("id")Long id) {
        SpuDTO spuDTO = spuService.querySpuDetailAndSkuById(id);
        return ResponseEntity.ok(spuDTO);
    }

    /**
     * 更新商品
     * @param spuDTO 页面提交的商品信息
     * @return 无
     */
    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuDTO spuDTO) {
        spuService.updateGoods(spuDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据id查询商品详情
     * @param id spuId
     * @return 商品详情对象
     */
    @GetMapping("/spu/detail")
    public ResponseEntity<SpuDetailDTO> queryDetailById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(new SpuDetailDTO(spuDetailService.getById(id)));
    }

    /**
     * 根据spuId查询sku集合
     * @param id spuId
     * @return sku的集合
     */
    @GetMapping("/sku/of/spu")
    public ResponseEntity<List<SkuDTO>> querySkuBySpuId(@RequestParam("id") Long id) {
        List<Sku> list = skuService.query().eq("spu_id", id).list();
        return ResponseEntity.ok(SkuDTO.convertEntityList(list));
    }

    /**
     * 根据id批量查询sku
     * @param ids sku的id集合
     * @return sku的集合
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<SkuDTO>> querySkuByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(SkuDTO.convertEntityList(skuService.listByIds(ids)));
    }

    /**
     * 根据商品id查询规格参数键值对
     * @param id 商品spuId
     * @param searching 是否参与搜索
     * @return 参数键值对
     */
    @GetMapping("/spec/value")
    public ResponseEntity<List<SpecParamDTO>> querySpecValues(
            @RequestParam("id") Long id, @RequestParam(value = "searching", required = false) Boolean searching){
        return ResponseEntity.ok(spuDetailService.querySpecValues(id, searching));
    }

    /**
     * 根据id查询spu
     * @param id spu的id
     * @return spu对象
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuDTO> querySpuById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new SpuDTO(spuService.getById(id)));
    }

    /**
     * 扣减库存
     * @param cartMap 商品集合
     */
    @PutMapping("/stock/minus")
    public ResponseEntity<Void> deductStock(@RequestBody Map<Long, Integer> cartMap){
        skuService.deductStock(cartMap);
        return ResponseEntity.noContent().build();
    }

    /**
     * 批量恢复库存
     * @param skuMap 恢复库存的商品及数量
     */
    @PutMapping("/stock/plus")
    public ResponseEntity<Void> addStock(@RequestBody Map<Long, Integer> skuMap){
        // 恢复库存
        skuService.addStock(skuMap);
        // 返回204
        return ResponseEntity.noContent().build();
    }

}
