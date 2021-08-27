package com.diao.myhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket docket(ApiInfo apiInfo,Environment environment) {
        // 获取当前环境
        Profiles isDev = Profiles.of("dev");
        boolean flag = environment.acceptsProfiles(isDev);
//        System.out.println(flag);
        return new Docket(DocumentationType.OAS_30)
                // 根据环境开启或关闭swagger Docket
                .enable(flag)
                // 设置组名
                .apiInfo(apiInfo)
                .select().apis(RequestHandlerSelectors.
                        // withClassAnnotation(Class<T>)扫描类的注解
                        // withMethodAnnotation(Class<T>)扫描方法注解
                        // basePackage(String path)扫描包
                        // any(),none()
                                basePackage("com.diao.myhub.controller")).build();
                //.paths(PathSelectors.ant("") 扫描路径;
    }
    @Bean
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .contact(new Contact("小王","http://www.xiaowang.com", "15982883012@139.com"))
                .description("myHub")
                .title("myhub社区测试").build();
    }
}
