package com.leyou.item.controller;

import com.leyou.item.service.SpuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // 一个测试启动器，可以加载Springboot测试注解。
@SpringBootTest
public class GoodsControllerTest {
    @Autowired
    private SpuService spuService;

    @Test
    public void querySpuByPage() {
        spuService.querySpuByPage(1,100,true,null,null,null);
    }

    @Test
    public void saveGoods() {
    }

    @Test
    public void updateSaleable() {
    }

    @Test
    public void querySpuDetailAndSkuById() {
    }

    @Test
    public void updateGoods() {
    }

    @Test
    public void queryDetailById() {
    }

    @Test
    public void querySkuBySpuId() {
    }

    @Test
    public void querySkuByIds() {
    }

    @Test
    public void querySpecValues() {
    }

    @Test
    public void querySpuById() {
    }

    @Test
    public void deductStock() {
    }

    @Test
    public void addStock() {
    }
}