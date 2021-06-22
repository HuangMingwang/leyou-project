package com.leyou.auth.service;

import com.leyou.auth.dto.AliOssSignatureDTO;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 9:54 下午
 */

public interface AliAuthService {
    AliOssSignatureDTO getSignature();
}
