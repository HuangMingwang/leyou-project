package com.leyou.search.client;

import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.dto.SpecParamDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-30 11:05 上午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemClientTest {

    @Autowired
    private ItemClient itemClient;



    @Test
    public void testFeign(){
        //List<CategoryDTO> list = itemClient.queryCategoryByIds(Arrays.asList(1L, 2L, 3L));

        //list.forEach(System.out::println);



    }
}
