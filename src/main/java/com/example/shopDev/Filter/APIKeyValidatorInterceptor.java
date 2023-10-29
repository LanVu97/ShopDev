package com.example.shopDev.Filter;

import com.example.shopDev.Config.HeaderContent;
import com.example.shopDev.Exception.InvalidAPIKeyException;
import com.example.shopDev.Models.ApiKey;
import com.example.shopDev.Repositories.ApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class APIKeyValidatorInterceptor implements HandlerInterceptor {

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Value("${app.apiKey.permission}")
    String permisson;

    private Logger logger = LoggerFactory.getLogger(APIKeyValidatorInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("preHandle");
        String apiKeyHeader = request.getHeader(HeaderContent.API_KEY);
        if(apiKeyHeader == null) {
            throw new InvalidAPIKeyException("Forbidden Error 1");
        }

        ApiKey apiKey = apiKeyRepository.findByKeyAndStatus(apiKeyHeader, true);

        if(apiKey == null) {
            throw new InvalidAPIKeyException("Forbidden Error 2");
        }

        // check permission
        if(!apiKey.getPermissions().contains(permisson)){
            throw new InvalidAPIKeyException("Forbidden Error 3");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        logger.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("afterCompletion");
    }

}