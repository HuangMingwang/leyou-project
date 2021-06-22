package com.leyou.item.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 5:44 下午
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;
    private final CategoryBrandService categoryBrandService;

    public BrandController(BrandService brandService, CategoryBrandService categoryBrandService) {
        this.brandService = brandService;
        this.categoryBrandService = categoryBrandService;
    }

    /**
     * 根据品牌id查询品牌
     *
     * @param id 品牌id
     * @return 品牌对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> queryBrandById(@PathVariable("id") Long id) {
        Brand brand = brandService.getById(id);
        return ResponseEntity.ok(new BrandDTO(brand));
    }

    /**
     * 根据品牌id的集合查询品牌集合
     *
     * @param idList 品牌id的集合
     * @return BrandDTO 集合
     */
    @GetMapping("/list")
    public ResponseEntity<List<BrandDTO>> queryBrandByIds(@RequestParam("ids") List<Long> idList) {
        return ResponseEntity.ok(BrandDTO.convertEntityList(brandService.listByIds(idList)));
    }

    /**
     * 分页查询品牌
     *
     * @param key  搜索条件可有可无，name或着letter
     * @param page 当前页码
     * @param rows 每页大小
     * @return 品牌分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<PageDTO<BrandDTO>> queryBrandByPage(@RequestParam(value = "key", required = false) String key,
                                                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                              @RequestParam(value = "rows", required = false, defaultValue = "5") Integer rows) {
        return ResponseEntity.ok(brandService.queryBrandByPage(key, page, rows));
    }

    /**
     * 根据分类id查询品牌集合
     *
     * @param id 分类id
     * @return BrandDTO 品牌集合
     */
    @GetMapping("/of/category")
    public ResponseEntity<List<Brand>> queryBrandByCategoryId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(brandService.queryBrandByCategoryId(id));
    }

    /**
     * 新增品牌
     *
     * @param brandDTO
     * @return 无
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(BrandDTO brandDTO) {
        brandService.saveBrand(brandDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新品牌
     *
     * @param brandDTO
     * @return 无
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(BrandDTO brandDTO) {
        brandService.updateBrand(brandDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据id删除品牌，中间表及redis中的数据
     *
     * @param id 要删除的品牌id
     * @return 无
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable("id") Long id) {
        brandService.removeByBrandId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
