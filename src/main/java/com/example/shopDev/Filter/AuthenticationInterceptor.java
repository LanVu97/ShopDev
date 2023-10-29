package com.example.shopDev.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.shopDev.Config.HeaderContent;
import com.example.shopDev.Exception.AuthFailError;
import com.example.shopDev.Exception.InvalidAPIKeyException;
import com.example.shopDev.Models.ApiKey;
import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.KeyTokenRepository;
import com.example.shopDev.Security.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    JsonWebToken jsonWebToken;

    @Autowired
    KeyTokenRepository keyTokenRepository;

    private Logger logger = LoggerFactory.getLogger(APIKeyValidatorInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("preHandle Authentication Filter");
//
            String accessTokenHeader = request.getHeader(HeaderContent.AUTHORIZATION);
            if(accessTokenHeader == null) {
                throw new AuthFailError("Invalid Request");
            }
            // get ShopId from token
        DecodedJWT decodedJWT_1 = JWT.decode(accessTokenHeader);
        String shopId = decodedJWT_1.getClaims().get("shopId").asString();
        logger.info("shopId:" + shopId);

        KeyToken keyToken = keyTokenRepository.findByShopId(shopId);
        String publicKey = keyToken.getPublicKey();
//
        Shops decodeShop = jsonWebToken.verifyToken(accessTokenHeader, publicKey);
            //gan keyStore cho request
        request.setAttribute("shop", decodeShop.getId());
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
