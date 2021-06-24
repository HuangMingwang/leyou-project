package com.leyou.user.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyou.common.dto.PageDTO;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.service.UserService;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // 一个测试启动器，可以加载Springboot测试注解。
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private UserService userService;


    @Test
    public void testSendVerifyCode() {
    }

    @Test
    public void testInfoExists() {
    }

    @Test
    public void testRegister() {
    }

    @Test
    public void testQueryUserByNameAndPassword() {
    }

    @Test
    public void testQueryUserByPage() {
        //创建分页对象
        Page<User> info =  new Page<>(1,2);
        PageDTO<UserDTO> userDTOPageDTO = userService.queryUserByPage(info);
        int total = userDTOPageDTO.getItems().size();
        int totalPage=total/2;
        System.out.println(total+"-----------"+totalPage);
        System.out.println(userDTOPageDTO);
    }

}