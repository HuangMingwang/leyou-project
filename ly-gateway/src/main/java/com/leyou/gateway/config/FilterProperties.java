package com.leyou.gateway.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author Huang Mingwang
 * @create 2021-06-07 10:03 下午
 */
@Component
@Data
@NoArgsConstructor
@ConfigurationProperties("ly.filter")
public class FilterProperties {
    private Map<String, Set<String>> allowRequests;
}
