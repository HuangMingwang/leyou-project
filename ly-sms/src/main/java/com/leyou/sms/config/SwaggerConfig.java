package com.leyou.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Huang Mingwang
 * @create 2021-06-27 16:29
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        // DocumentationType.SWAGGER_2 固定的，代表swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                // 如果配置多个文档的时候，那么需要配置groupName来分组标识
                .groupName("分布式任务系统")
                // 用于生成API信息
                .apiInfo(apiInfo())
                // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
                .select()
                // 用于指定扫描哪个包下的接口
                .apis(RequestHandlerSelectors.basePackage("com.leyou.sms.controller"))
                // 选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     * @return api的基本信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //  可以用来自定义API的主标题
                .title("短信服务API")
                // 可以用来描述整体的API
                .description("短信服务SwaggerAPI管理")
                // 用于定义服务的域名
                .termsOfServiceUrl("")
                // 可以用来定义版本。
                .version("1.0.0")
                .build();
    }
}
