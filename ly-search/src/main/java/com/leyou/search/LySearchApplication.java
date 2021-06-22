package com.leyou.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Huang Mingwang
 * @create 2021-05-30 9:58 上午
 */
@EnableFeignClients("com.leyou.item.client")
@SpringBootApplication(scanBasePackages = {"com.leyou.search","com.leyou.common.advice"})
public class LySearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySearchApplication.class, args);
    }
}
