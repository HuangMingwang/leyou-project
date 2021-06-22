package com.leyou.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Huang Mingwang
 * @create 2021-06-04 4:06 下午
 */
@Configuration
@EnableConfigurationProperties(value = SmsProperties.class)
public class SmsConfig {
    @Bean
    public IAcsClient getIAcsClient(SmsProperties smsProperties) {
        return new DefaultAcsClient(DefaultProfile.getProfile(smsProperties.getRegionID(),
                smsProperties.getAccessKeyID(), smsProperties.getAccessKeySecret()));
    }
}
