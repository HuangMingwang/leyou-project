package com.leyou.page;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Huang Mingwang
 * @create 2021-06-01 2:46 下午
 */
@EnableFeignClients("com.leyou.item.client")
@SpringBootApplication(scanBasePackages = {"com.leyou.page", "com.leyou.common.advice"})
public class LyPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyPageApplication.class, args);
    }

}
