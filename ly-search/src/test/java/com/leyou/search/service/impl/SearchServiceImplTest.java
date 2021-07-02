package com.leyou.search.service.impl;

import com.leyou.search.service.SearchService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceImplTest extends TestCase {
    @Autowired
    private SearchService searchService;
    @Test
    public void testCreateIndexAndMapping() {
        searchService.createIndexAndMapping();
    }
    @Test
    public void testLoadData() {
        searchService.loadData();
    }

    public void testGetSuggestion() {
    }

    public void testGetCommonSearch() {
    }

    public void testSearchGoods() {
    }

    public void testGetFilters() {
    }

    public void testSaveGoodsById() {
    }

    public void testDeleteGoodsById() {
    }

    public void testSpuDTOToGoods() {
    }
}