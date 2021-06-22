package com.leyou.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 9:31 下午
 */
@EnableFeignClients(basePackages = "com.leyou.user.client")
@SpringBootApplication(scanBasePackages = {"com.leyou.auth", "com.leyou.common.advice"})
public class LyAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyAuthApplication.class, args);
    }
}
