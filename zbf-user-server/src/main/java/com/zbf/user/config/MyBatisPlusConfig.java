package com.zbf.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 每次记得修改mapper自己的扫描包
 */
@Configuration
@MapperScan("com.zbf.user.mapper")
public class MyBatisPlusConfig implements WebMvcConfigurer {
    //datasource: dbcp c3p0,druid,Hikari,BoneCp
    /**
     * 自定义静态资源方法
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:D://pic//","file:D://1801dtest//");
    }
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
