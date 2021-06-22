package com.leyou.trade.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Huang Mingwang
 * @create 2021-06-11 6:16 下午
 */
@Configuration
public class WxConfig {

    @Bean
    @ConfigurationProperties(prefix = "ly.pay.wx")
    public WXPayConfigImpl payConfig(){
        return new WXPayConfigImpl();
    }

    @Bean
    public WXPay wxPay(WXPayConfigImpl payConfig){
        try {
            return new WXPay(payConfig, payConfig.getNotifyUrl());
        } catch (Exception e) {
            throw new RuntimeException("构建微信工具失败！");
        }
    }
}
