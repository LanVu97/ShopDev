package com.example.shopDev.Services;

import com.auth0.jwt.interfaces.Claim;
import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.Exception.AuthFailError;
import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Exception.ForbidenError;
import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.ShopRepository;
import com.example.shopDev.Security.Hashing;
import com.example.shopDev.Security.JsonWebToken;
import com.example.shopDev.Security.PairKey;
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


    public Shops SignUp(Shops shop) throws NoSuchAlgorithmException, InvalidKeySpecException {

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
//            PairKey pairKey = PairKey.createPairKey();
//
//            //save to db, return publickey like string.
//            String publicKeyString = keyTokenService.createKeyToken(newShop , pairKey.getPublicKey(), pairKey.getPrivateKey());
//
//            if(publicKeyString == null){
//                throw new BadRequestError("Error when saving publicKey String");
//            }

            return newShop;

    }

    public Map<String, Object> Login(Shops shopEntity) throws NoSuchAlgorithmException, InvalidKeySpecException {
        /*
        1. check email
        2. check password
        3. create publicKey, privateKey
        4. Create Tokens

         */

        String email = shopEntity.getEmail();
        Shops shop = shopRepository.findByEmail(email);
        // check email
        if(!shopRepository.existsByEmail(email)){
            throw new BadRequestError("Shop not register");
        }

        // check password
        String passInDB = shop.getPassword();
        if(!Hashing.validatePassword(shopEntity.getPassword(), passInDB)){
            throw new AuthFailError("Authentiction error");
        }

        // Get public and private key pair
        PairKey pairKey = PairKey.createPairKey();

        // Create Token Payload
        Map<String, String> tokenPayload = new Hashtable<>();
        String x = shop.getId();
        tokenPayload.put("shopId", shop.getId());
        tokenPayload.put("email", shop.getEmail());

        //Create accessToken, refreshToken
        Map<String, String> tokens = JsonWebToken.createTokenPair(tokenPayload, pairKey.getPrivateKey());

        //save to db, return publickey like string.
        String publicKeyString = keyTokenService.createKeyToken(shop ,
                pairKey.getPublicKey(),
                pairKey.getPrivateKey(),
                tokens.get("refreshToken")
                );

        if(publicKeyString == null){
                throw new BadRequestError("Error when saving publicKey String");
            }
        // return token
        Map<String, Object> res = new Hashtable<>();
        res.put("shop", shop);
        res.put("tokens", tokens);
        return res;
    }

    public Map<String, Object> HandleRefreshToken(String refreshToken) throws NoSuchAlgorithmException, InvalidKeySpecException{
        /*
        1. check RT co trong list used ko?
        2. check RT co dung ko
        3. Neu dung cap lai AT, RT moi
         */

        //1. check RT co trong list used ko?
       if( keyTokenService.isInUsed(refreshToken)){
           // delete all KeyToken of shop
           keyTokenService.deleteByRefreshTokenUsed(refreshToken);
           throw new ForbidenError("Something wrong, Please re-login");
       }

       //2. check RT co dung ko
        KeyToken holderToken = keyTokenService.findByRefreshToken(refreshToken);
       if(holderToken == null){
           throw new AuthFailError("Shop not register");
       }

       //verify Token
        // check
        Shops shop = JsonWebToken.verifyToken(refreshToken, holderToken.getPublicKey());

        // create 1 cap moi token moi
        // Create Token Payload
        Map<String, String> tokenPayload = new Hashtable<>();
        tokenPayload.put("shopId", shop.getId());
        tokenPayload.put("email", shop.getEmail());
        Map<String, String> tokens = JsonWebToken.createTokenPair(tokenPayload, holderToken.getPrivateKey());

        // update keyToken
        //save to db, return publickey like string.
        keyTokenService.updateKeyToken(shop ,
                refreshToken,
                tokens.get("refreshToken")
        );
        // return token
        Map<String, Object> res = new Hashtable<>();
        res.put("shop", shop);
        res.put("tokens", tokens);
        return res;
    }
}
