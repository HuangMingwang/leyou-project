package com.leyou.auth.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Huang Mingwang
 * @create 2021-05-24 9:36 上午
 */
@Configuration
public class AliOssConfig {
    @Bean
    public OSS ossClient(AliOssProperties properties) {
        return new OSSClientBuilder().
                build(properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getAccessKeySecret());
    }
}
