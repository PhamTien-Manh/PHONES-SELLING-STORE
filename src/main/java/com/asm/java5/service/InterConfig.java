package com.asm.java5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterConfig implements WebMvcConfigurer {
    @Autowired
    GlobalInterceptor global;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(global)
                .addPathPatterns("/**")
                .excludePathPatterns("/assets/**", "/index/**","/page-contact","/page-faq"
                ,"/products/**", "/login", "/register", "/forgot-pass");
    }


}

