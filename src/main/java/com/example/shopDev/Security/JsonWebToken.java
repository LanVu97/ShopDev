package com.example.shopDev.Security;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
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

    private static Map<String, Claim> verifyToken(String token, RSAPublicKey publicKey) {

        try {

            Algorithm algorithm = Algorithm.RSA512(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

                DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaims();

        } catch (JWTVerificationException exception) {
            System.out.println("claim error:" + exception);

        }
        return null;
    }

// create token pair
    public static Map<String, String> createTokenPair(Map<String, String> payload, RSAPublicKey publicKey, RSAPrivateKey privateKey){
        try{
            // accessToken
            String accessToken = generateToken(privateKey, payload, 5000L);

            // refreshToken
            String refreshToken = generateToken(privateKey, payload, 8000L);

//            // verify
//            String claim = verifyToken(accessToken, publicKey).asString();
//            System.out.println("claim " + claim);
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            return map;
        }catch(Exception ex){
            System.out.println("exception error" + ex);
            throw ex;
        }
}

// convert String to Object
    public static RSAPublicKey getPublicKeyFromString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String publicKeyPEM = key;

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));

        return pubKey;


    }
}
