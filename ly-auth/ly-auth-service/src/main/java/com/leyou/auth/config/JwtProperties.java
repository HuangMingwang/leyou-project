package com.leyou.auth.config;

import com.leyou.common.utils.RsaUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
/**
 * @author Huang Mingwang
 * @create 2021-06-06 9:31 下午
 */
@Data
@NoArgsConstructor
@Slf4j
@ConfigurationProperties("ly.jwt")
@Component
public class JwtProperties implements InitializingBean {
    private String pubKeyPath;
    private String priKeyPath;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Override
    public void afterPropertiesSet() {
        try {
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("获取秘钥失败,失败原因:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
