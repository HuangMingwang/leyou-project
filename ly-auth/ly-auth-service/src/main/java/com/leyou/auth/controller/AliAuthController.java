package com.leyou.auth.controller;

import com.leyou.auth.dto.AliOssSignatureDTO;
import com.leyou.auth.service.AliAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 9:50 下午
 */
@RestController
@RequestMapping("/ali")
public class AliAuthController {
    private final AliAuthService aliAuthService;

    public AliAuthController(AliAuthService aliAuthService) {
        this.aliAuthService = aliAuthService;
    }

    @GetMapping("/oss/signature")
    public ResponseEntity<AliOssSignatureDTO> getSignature() {
        return ResponseEntity.ok(aliAuthService.getSignature());
    }
}
