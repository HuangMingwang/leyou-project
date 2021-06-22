package com.leyou.item.client;

import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Feign的原理: 对 http 请求的伪装
 * 需要知道：localhost:8081/goods/spu/page?page=1
 * - 主机和端口：通过@FeignClient("item-service")得到服务名称，去eureka根据服务名称拉取服务列表
 * - 请求方式： @GetMapping
 * - 请求路径：@GetMapping("/goods/spu/page")
 * - 请求参数：@RequestParam(value = "page", defaultValue = "1") Integer page
 * - 返回值类型：响应体的类型
 *
 * @author Huang Mingwang
 * @create 2021-05-30 10:56 上午
 */
@FeignClient("item-service")
public interface ItemClient {
    /**
     * 根据id查询品牌
     *
     * @param id 品牌的id
     * @return 品牌对象
     */
    @GetMapping("/brand/{id}")
    BrandDTO queryBrandById(@PathVariable("id") Long id);

    /**
     * 根据id的查询商品分类
     *
     * @param id 商品分类的id集
     * @return 分类
     */
    @GetMapping("/category/{id}")
    CategoryDTO queryCategoryById(@PathVariable("id") Long id);

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
    @GetMapping("/goods/spu/page")
    PageDTO<SpuDTO> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "id", required = false) Long id);

    /**
     * 根据spuID查询spuDetail
     *
     * @param id spuID
     * @return SpuDetail
     */
    @GetMapping("/goods/spu/detail")
    SpuDetailDTO querySpuDetailById(@RequestParam("id") Long id);

    /**
     * 根据spuID查询sku
     *
     * @param id spuID
     * @return sku的集合
     */
    @GetMapping("/goods/sku/of/spu")
    List<SkuDTO> querySkuBySpuId(@RequestParam("id") Long id);

    /**
     * 查询规格参数
     *
     * @param groupId    组id
     * @param categoryId 分类id
     * @param searching  是否用于搜索
     * @return 规格组集合
     */
    @GetMapping("/spec/params")
    List<SpecParamDTO> querySpecParams(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "searching", required = false) Boolean searching
    );

    /**
     * 根据spuId查询spu的所有规格参数值
     *
     * @param id        spu的id
     * @param searching 是否参与搜索
     * @return 规格参数值
     */
    @GetMapping("/goods/spec/value")
    List<SpecParamDTO> querySpecValues(
            @RequestParam("id") Long id,
            @RequestParam(value = "searching", required = false) Boolean searching);

    /**
     * 根据分类id查询分类集合
     *
     * @param idList id集合
     * @return category集合
     */
    @GetMapping("/category/list")
    List<CategoryDTO> queryCategoryByIds(@RequestParam("ids") List<Long> idList);

    /**
     * 根据品牌id查询分类集合
     *
     * @param idList id集合
     * @return category集合
     */
    @GetMapping("/brand/list")
    List<BrandDTO> queryBrandByIds(@RequestParam("ids") List<Long> idList);

    /**
     * 根据id批量查询sku
     *
     * @param ids skuId的集合
     * @return sku的集合
     */
    @GetMapping("/goods/sku/list")
    List<SkuDTO> querySkuByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据id查询商品
     *
     * @param id 商品id
     * @return 商品信息
     */
    @GetMapping("/goods/{id}")
    SpuDTO queryGoodsById(@PathVariable("id") Long id);

    /**
     * 根据id查询spu，不包含别的
     *
     * @param id 商品id
     * @return spu
     */
    @GetMapping("/goods/spu/{id}")
    SpuDTO querySpuById(@PathVariable("id") Long id);

    /**
     * 根据分类id查询规格组及组内参数
     *
     * @param id 分类id
     * @return 组及组内参数
     */
    @GetMapping("/spec/list")
    List<SpecGroupDTO> querySpecGroupDetailByCategoryId(@RequestParam("id") Long id);

    /**
     * 减库存
     * @param cartMap 商品id及数量的map
     */
    @PutMapping("/goods/stock/minus")
    void deductStock(@RequestBody Map<Long, Integer> cartMap);

    /**
     * 批量恢复库存
     * @param skuMap 恢复库存的商品及数量
     */
    @PutMapping("/goods/stock/plus")
    void addStock(@RequestBody Map<Long, Integer> skuMap);
}
