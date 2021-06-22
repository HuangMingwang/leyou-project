package com.leyou.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 要让这个类生效，必须让项目可以扫描到这个类所在的包，将其加入spring容器
 * @author Huang Mingwang
 * @create 2021-06-18 11:24 上午
 */
@MapperScan("com.leyou.item.mapper")
@SpringBootApplication(scanBasePackages = {"com.leyou.item", "com.leyou.common.advice"})
public class LyItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class, args);
    }
}
