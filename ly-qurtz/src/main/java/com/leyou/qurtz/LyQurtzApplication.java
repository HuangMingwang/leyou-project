package com.leyou.qurtz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Huang Mingwang
 * @create 2021-05-25 3:37 下午
 */
@SpringBootApplication(scanBasePackages = {"com.leyou.common.advice", "com.leyou.qurtz"})
public class LyQurtzApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyQurtzApplication.class, args);
    }
}
