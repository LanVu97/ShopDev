package com.example.shopDev.Security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.KeyTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Component
public class JsonWebToken {

    @Autowired
    KeyTokenRepository keyTokenRepository;

    public String generateToken(RSAPrivateKey privateKey, Map<String, String> payload, Long time) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.RSA512(null, privateKey);

            token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject("Token Details")
                    .withPayload(payload)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + time))
                    .withJWTId(UUID.randomUUID()
                            .toString())
                    .sign(algorithm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }


// create token pair
    public  Map<String, String> createTokenPair(Map<String, String> payload, String privateKey){

            RSAPrivateKey rsaPrivateKey = PairKey.getPrivateKeyFromString(privateKey);
            // accessToken
            String accessToken = generateToken(rsaPrivateKey, payload, 500000L);

            // refreshToken
            String refreshToken = generateToken(rsaPrivateKey, payload, 80000000L);

            Map<String, String> map = new HashMap<>();
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            return map;
}

    // verify and get claims using public key

    public Shops verifyToken(String token, String publicKey){

        try {

            RSAPublicKey rsaPublicKey = PairKey.getPublicKeyFromString(publicKey);

            Algorithm algorithm = Algorithm.RSA512(rsaPublicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            System.out.println("2222222222222222" + decodedJWT);
            Map<String, Claim> map =  decodedJWT.getClaims();

            return Shops.builder()
                    .id(map.get("shopId").asString())
                    .email(map.get("email").asString())
                    .build();

        } catch (JWTVerificationException exception) {
            System.out.println("claim error:" + exception);
            throw new RuntimeException(exception);

        }
    }

}
