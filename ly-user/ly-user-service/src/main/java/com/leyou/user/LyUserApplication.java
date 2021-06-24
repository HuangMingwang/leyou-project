package com.leyou.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Huang Mingwang
 * @create 2021-06-05 5:32 下午
 */
@MapperScan(basePackages = {"com.leyou.user.mapper","com.leyou.user.config"})
@SpringBootApplication(scanBasePackages = {"com.leyou.user", "com.leyou.common.advice"})
@EnableSwagger2
public class LyUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyUserApplication.class,args);
    }
}
