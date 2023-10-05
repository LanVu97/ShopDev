package com.example.shopDev.Filter;

import java.io.IOException;
import java.util.List;
import javax.naming.AuthenticationException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.shopDev.Config.HeaderContent;
import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.DTO.ErrorObject;
import com.example.shopDev.Exception.InvalidAPIKeyException;
import com.example.shopDev.Models.ApiKey;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.ApiKeyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class APIKeyValidatorFilter implements Filter {

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Value("${app.apiKey.permission}")
    String permisson;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

//        ApiKey keyEntity = ApiKey.builder()
//                .key("dfsdfdsfd")
//                .status(true)
//                .permissions(List.of("0000"))
//                .build();
//
//        apiKeyRepository.save(keyEntity);
        try{
            HttpServletRequest req = (HttpServletRequest) request;
            String apiKeyHeader = req.getHeader(HeaderContent.API_KEY);
            System.out.println("hhhhh" + apiKeyHeader);
            if(apiKeyHeader == null) {
                throw new InvalidAPIKeyException("Forbidden Error");
            }

            ApiKey apiKey = apiKeyRepository.findByKeyAndStatus(apiKeyHeader, true);

            if(apiKey == null) {
                throw new InvalidAPIKeyException("Forbidden Error");
            }

            // check permission
            if(!apiKey.getPermissions().contains(permisson)){

                throw new InvalidAPIKeyException("Forbidden Error");
            }

            chain.doFilter(request, response);

        }catch (InvalidAPIKeyException e){
            ErrorObject errorEntity = ErrorObject.builder()
                    .code(e.getCode())
                    .status("error")
                    .message(e.getMessage())
                    .build();
            ((HttpServletResponse) response).setContentType("application/json");
            ((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());
            ((HttpServletResponse) response).getWriter().write(objectMapper.writeValueAsString(errorEntity));

        }

    }
}