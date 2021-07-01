package com.leyou.user.web;

import com.leyou.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        System.out.println(userService.queryUserByPage("18296962694", 1, 5));
    }

    @Test
    public void testCountUser() {
        System.out.println(userService.countUser());
    }

}