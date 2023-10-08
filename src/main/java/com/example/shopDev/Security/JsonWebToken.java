package com.example.shopDev.Security;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class JsonWebToken {

    public static String generateToken(RSAPrivateKey privateKey, Map<String, String> payload, Long time) {
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

    // verify and get claims using public key

    public static Shops verifyToken(String token, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

//        try {
            RSAPublicKey rsaPublicKey = PairKey.getPublicKeyFromString(publicKey);

            Algorithm algorithm = Algorithm.RSA512(rsaPublicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

                DecodedJWT decodedJWT = verifier.verify(token);
        Map<String, Claim> map =  decodedJWT.getClaims();

        return Shops.builder()
                .id(map.get("shopId").asString())
                .email(map.get("email").asString())
                .build();


//        } catch (JWTVerificationException exception) {
//            System.out.println("claim error:" + exception);
//
//        }
//        return null;
    }

// create token pair
    public static Map<String, String> createTokenPair(Map<String, String> payload, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        try{
            RSAPrivateKey rsaPrivateKey = PairKey.getPrivateKeyFromString(privateKey);
            // accessToken
            String accessToken = generateToken(rsaPrivateKey, payload, 5000L);

            // refreshToken
            String refreshToken = generateToken(rsaPrivateKey, payload, 80000000L);

//            // verify
//            String claim = verifyToken(accessToken, publicKey).asString();
//            System.out.println("claim " + claim);
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            return map;
//        }catch(Exception ex){
//            System.out.println("exception error" + ex);
//            throw new ex;
//        }
}





}
