package com.leyou.auth.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.leyou.auth.config.AliOssProperties;
import com.leyou.auth.dto.AliOssSignatureDTO;
import com.leyou.auth.service.AliAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 9:55 下午
 */
@Service
@Slf4j
public class AliAuthServiceImpl implements AliAuthService {
    private final AliOssProperties aliOssProperties;
    private final OSS ossClient;

    public AliAuthServiceImpl(AliOssProperties aliOssProperties, OSS ossClient) {
        this.aliOssProperties = aliOssProperties;
        this.ossClient = ossClient;
    }

    @Override
    public AliOssSignatureDTO getSignature() {
        // 创建OSSClient实例。
        try {
            long expireTime = aliOssProperties.getExpireTime();
            long expireEndTime = System.currentTimeMillis() + expireTime;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            //上传大小限制
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            //上传文件夹
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, aliOssProperties.getDir());
            //生成策略
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            //转换二进制
            byte[] binaryData = postPolicy.getBytes("utf-8");
            //base64编码
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            //对policy生成签名
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            //id, 主机, 策略, 签名, 桶
            return AliOssSignatureDTO.of(aliOssProperties.getAccessKeyId(), aliOssProperties.getHost(),encodedPolicy, postSignature, expireEndTime, aliOssProperties.getDir());
        } catch (Exception e) {
            log.error("生成签名失败,原因是:{}",e.getMessage(), e);
            throw new RuntimeException("图片上传失败", e);
        } finally {
            ossClient.shutdown();
        }
    }
}
