package com.leyou.item.controller;

import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-22 2:50 下午
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 根据id查询分类
     *
     * @param id
     * @return CategoryDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> queryCategoryById(@PathVariable("id") Long id) {
        Category category = categoryService.getById(id);
        return ResponseEntity.ok(new CategoryDTO(category));
    }

    /**
     * 根据id集合查询分类
     *
     * @param ids id集合
     * @return DTO集合
     */
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByIds(@RequestParam(value = "ids") List<Long> ids) {
        List<Category> categories = categoryService.listByIds(ids);
        List<CategoryDTO> categoryDTOS = CategoryDTO.convertEntityList(categories);
        return ResponseEntity.ok(categoryDTOS);
    }

    /**
     * 根据父类目id查询子类目集合
     *
     * @param id 父类目id
     * @return 子类目集合
     */
    @GetMapping("/of/parent")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByPId(@RequestParam(value = "pid") Long id) {
        List<CategoryDTO> categoryDTO = CategoryDTO.convertEntityList(categoryService.lambdaQuery().
                eq(Category::getParentId, id).list());
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/of/brand")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByBrandId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(categoryService.queryCategoryByBrandId(id));
    }
}
