package com.leyou.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Huang Mingwang
 * @create 2021-06-4 4:02 下午
 */
@SpringBootApplication(scanBasePackages = {"com.leyou.sms", "com.leyou.common.advice"})
public class LySmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySmsApplication.class, args);
    }
}
