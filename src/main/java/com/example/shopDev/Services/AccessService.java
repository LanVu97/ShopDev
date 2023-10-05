package com.example.shopDev.Services;

import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.ShopRepository;
import com.example.shopDev.Security.Hashing;
import com.example.shopDev.Security.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Service
public class AccessService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    KeyTokenService keyTokenService;


    public Map<String, Object> SignUp(Shops shop) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        try {
            // check email exist
            boolean holderShop = shopRepository.existsByEmail(shop.getEmail());

            if(holderShop){
                throw new BadRequestError("Error: Shop already in use");
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

            Shops newShop = shopRepository.save(shopEntity);

            // Get public and private key pair
            String algorithm = "RSA";
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            //save to db, return publickey like string.
            String publicKeyString = keyTokenService.createKeyToken(newShop , rsaPublicKey);

            if(publicKeyString == null){
                throw new BadRequestError("Error when saving publickey String");
            }
            // covert public key to RSA object
            rsaPublicKey = JsonWebToken.getPublicKeyFromString(publicKeyString);


            // Create Token Payload
            Map<String, String> tokenPayload = new Hashtable<>();
            String x = newShop.getId();
            tokenPayload.put("shopId", newShop.getId());
            tokenPayload.put("email", newShop.getEmail());
            //Create accessToken, refreshToken
            Map<String, String> tokens = JsonWebToken.createTokenPair(tokenPayload, rsaPublicKey, privateKey);

            // return token
            Map<String, Object> res = new Hashtable<>();
            res.put("shop", newShop);
            res.put("tokens", tokens);
            return res;
//        }catch (Exception ex){
//            throw new Exception(ex);
//
//
//        }
    }
}
