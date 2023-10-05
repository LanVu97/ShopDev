package com.example.shopDev.Config;

import com.example.shopDev.Filter.APIKeyValidatorFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FilterConfig {

//    @Bean
    public FilterRegistrationBean<APIKeyValidatorFilter> filterFilterRegistrationBean() {
        FilterRegistrationBean<APIKeyValidatorFilter> registrationBean = new FilterRegistrationBean<>();
        APIKeyValidatorFilter authFilter = new APIKeyValidatorFilter();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;

    }
}
