package com.leyou.sms.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Huang Mingwang
 * @create 2021-06-04 4:40 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUtilsTest {
    @Autowired
    private SmsUtils smsUtils;
    @Test
    public void sendVerifyCode() {
        smsUtils.sendVerifyCode("18296962694","12345");
    }
}