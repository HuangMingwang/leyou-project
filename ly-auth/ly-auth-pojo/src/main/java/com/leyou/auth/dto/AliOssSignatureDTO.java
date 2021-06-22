package com.leyou.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 9:40 下午
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AliOssSignatureDTO {
    /**
     * 用户的AccessKeyId
     */
    private String accessId;
    /**
     * 申请的阿里OSS的bucket的访问地址
     */
    private String host;
    /**
     * 文件上传的策略，主要包含对上传文件的要求，利用Base64加密后返回
     */
    private String policy;
    /**
     * 生成的签名
     */
    private String signature;
    /**
     * 本次签名的过期时间，客户端可以换成签名，在有效期内无需再次签名
     */
    private Long expire;
    /**
     * 要上传到bucket中的哪个目录
     */
    private String dir;
}
