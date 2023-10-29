package com.example.shopDev.Security;

import jdk.jshell.spi.ExecutionControl;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Data @AllArgsConstructor
public class PairKey {

    private String publicKey;
    private String privateKey;

    public static PairKey createPairKey() {

        try{
            String algorithm = "RSA";
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaprivateKey = (RSAPrivateKey) keyPair.getPrivate();

            String publicKey = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(rsaprivateKey.getEncoded());

            return new PairKey(publicKey, privateKey);

        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }

    }

    // convert String to Object
    public static RSAPublicKey getPublicKeyFromString(String key) {

        try{
            byte[] encoded = Base64.getDecoder().decode(key);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
            return pubKey;

        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }

    }

    // convert String to Object
    public static RSAPrivateKey getPrivateKeyFromString(String key){

        try{
            byte[] privateBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return  (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }

    }
}
