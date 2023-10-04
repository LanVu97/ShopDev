package com.example.shopDev.Security;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

    public static String generateToken(RSAPrivateKey privateKey, Shops shop, Long time) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.RSA512(null, privateKey);

            token = JWT.create()
                    .withIssuer("Lan")
                    .withSubject("Token Details")
                    .withClaim("userId", shop.getId())
                    .withClaim("email", shop.getEmail())
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

    private static Claim verifyToken(String token, RSAPublicKey publicKey) {
        Claim claims;
        try {

            Algorithm algorithm = Algorithm.RSA512(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Lan")
                    .build();

                DecodedJWT decodedJWT = verifier.verify(token);
            claims = decodedJWT.getClaim("userId");

        } catch (Exception e) {
            System.out.println("claim error:" + e);
            claims = null;
        }
        return claims;
    }

// create token pair
    public static Map<String, String> createTokenPair(Shops shop, RSAPublicKey publicKey, RSAPrivateKey privateKey){
        try{
            // accessToken
            String accessToken = generateToken(privateKey, shop, 5000L);

            // refreshToken
            String refreshToken = generateToken(privateKey, shop, 8000L);

            // verify
            String claim = verifyToken(accessToken, publicKey).asString();
            System.out.println("claim " + claim);
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
    public static RSAPublicKey getPublicKeyFromString(String key) throws
            InvalidKeySpecException, NoSuchAlgorithmException {

        String publicKeyPEM = key;

        /**replace headers and footers of cert, if RSA PUBLIC KEY in your case, change accordingly*/
//        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
//        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));

        return pubKey;

//        String publicK = “<PUBLIC_KEY_STRING>”;
//        byte[] publicBytes = Base64.getDecoder().decode(publicK);
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(“RSA”);
//        pubKey = keyFactory.generatePublic(keySpec);
    }
}
