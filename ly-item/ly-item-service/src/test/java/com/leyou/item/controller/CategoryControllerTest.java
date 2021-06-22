package com.leyou.item.controller;

import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Huang Mingwang
 * @create 2021-05-22 3:20 下午
 */
@RunWith(SpringRunner.class) // 一个测试启动器，可以加载Springboot测试注解。
@SpringBootTest
public class CategoryControllerTest {
    @Autowired
    CategoryController categoryController;
    @Autowired
    private CategoryService categoryService;
    @Test
    public void queryCategoryById() {
        Category byId = categoryService.getById(10L);
        System.out.println(byId);
    }



}