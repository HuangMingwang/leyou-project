package com.leyou.gateway.config;

import com.leyou.common.utils.RsaUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

/**
 * @author Huang Mingwang
 * @create 2021-06-07 9:46 下午
 */
@Data
@ConfigurationProperties("ly.jwt")
@Component
@NoArgsConstructor
@Slf4j
public class JwtProperties implements InitializingBean {

    private String pubKeyPath ;

    private PublicKey publicKey;


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            log.info("【网关】获取公钥成功");
        } catch (Exception e) {
            log.error("【网关】获取公钥失败", e);
            throw new RuntimeException(e);
        }

    }
}
