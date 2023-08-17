/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * The type My web config.
 */
@Configuration
@EnableWebMvc
public class MyWebConfig extends WebMvcConfigurerAdapter {
    /**
     * 日志管理
     */
    private Logger log = LoggerFactory.getLogger(MyWebConfig.class);

    /**
     * Add resource handlers.
     *
     * @param registry the registry
     * @description:
     * @author cheng
     * @dateTime 2018 /4/19 17:59
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("配置静态资源所在目录");
        // 和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        // 新版swagger需添加
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}