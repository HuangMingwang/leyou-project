package com.leyou.auth.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 9:34 上午
 */
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("ly.oss")
public class AliOssProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String host;
    private String endpoint;
    private String dir;
    private long expireTime;
    private long maxFileSize;
}
