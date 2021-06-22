package com.leyou.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huang Mingwang
 * @create 2021-05-20 10:35 上午
 */
@RestController
public class FallBackController {
    @GetMapping("/hystrix/fallback")
    public ResponseEntity<String> fallBackController() {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("网关请求超时");
    }
}
