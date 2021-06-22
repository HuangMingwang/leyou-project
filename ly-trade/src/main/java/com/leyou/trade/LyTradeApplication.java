package com.leyou.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 9:22 上午
 */
@EnableFeignClients(basePackages = {"com.leyou.item.client", "com.leyou.user.client"})
@MapperScan("com.leyou.trade.mapper")
@SpringBootApplication(scanBasePackages = {"com.leyou.trade", "com.leyou.common.advice"})
public class LyTradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyTradeApplication.class, args);
    }
}
