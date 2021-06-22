package com.leyou.trade.config;

import com.leyou.trade.interceptors.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Huang Mingwang
 * @create 2021-06-08 10:09 上午
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(new UserInterceptor()).excludePathPatterns("/pay/wx/notify");
    }
}
