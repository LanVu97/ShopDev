package com.example.shopDev.Services;

import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.AccessRepository;
import com.example.shopDev.Repositories.KeyTokenRepository;
import com.example.shopDev.Security.Hashing;
import com.example.shopDev.Security.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Service
public class AccessService {
    Map<String, String> roleShop = new Hashtable<>();

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    KeyTokenService keyTokenService;


    public Map<String, Object> SignUp(Shops shop) throws Exception {
        try {
            // check email exist
            boolean holderShop = accessRepository.existsByEmail(shop.getEmail());

            if(holderShop){
                throw new Exception("Email already in use");
                System.out.println("Email already in use");
                return null;
            }

            String passwordHash = Hashing.generatePasswordHash(shop.getPassword());
            Shops shopEntity = Shops.builder()
                    .name(shop.getName())
                    .email(shop.getEmail())
                    .password(passwordHash)
                    .isVerify(false)
                    .status(StatusShop.INACTIVE.getName())
                    .roles(List.of(RoleShop.SHOP.getName()))
                    .build();

            Shops newShop = accessRepository.save(shopEntity);

            // Get public and private key pair
            String algorithm = "RSA";
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            //save to db, return publickey like string.
            String publicKeyString = keyTokenService.createKeyToken(newShop , rsaPublicKey);

            if(publicKeyString == null){
                throw new RuntimeException("Error when saving publickey String");
            }
            // covert public key to RSA object
            rsaPublicKey = JsonWebToken.getPublicKeyFromString(publicKeyString);

            Map<String, String> tokens = JsonWebToken.createTokenPair(newShop, rsaPublicKey, privateKey);

            // return token
            Map<String, Object> res = new Hashtable<>();
            res.put("shop", newShop);
            res.put("tokens", tokens);
            return res;
        }catch (Exception ex){
            throw new Exception(ex);


        }
    }
}
