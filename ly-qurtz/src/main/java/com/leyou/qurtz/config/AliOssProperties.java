package com.leyou.qurtz.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 3:32 下午
 */
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("ly.oss")
public class AliOssProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;
}
