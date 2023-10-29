package com.example.shopDev.Config;

import com.example.shopDev.Filter.APIKeyValidatorInterceptor;
import com.example.shopDev.Filter.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    APIKeyValidatorInterceptor apiKeyValidatorInterceptor;

    @Autowired
    AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(apiKeyValidatorInterceptor);

        registry.addInterceptor(authenticationInterceptor)
//                .addPathPatterns("/v1/api/shop/")
                .excludePathPatterns("/v1/api/shop/login", "/v1/api/shop/signup");
    }


}