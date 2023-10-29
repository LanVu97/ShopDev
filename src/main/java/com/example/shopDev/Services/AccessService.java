package com.example.shopDev.Services;

import com.auth0.jwt.interfaces.Claim;
import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.Exception.AuthFailError;
import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Exception.ForbidenError;
import com.example.shopDev.Models.ApiKey;
import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.ApiKeyRepository;
import com.example.shopDev.Repositories.KeyTokenRepository;
import com.example.shopDev.Repositories.ShopRepository;
import com.example.shopDev.Security.Hashing;
import com.example.shopDev.Security.JsonWebToken;
import com.example.shopDev.Security.PairKey;
import org.apache.commons.lang3.RandomStringUtils;
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
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private Hashing hashing;

    @Autowired
    private KeyTokenRepository keyTokenRepository;

    @Autowired
    JsonWebToken jsonWebToken;


    public ApiKey CreateAPIKey(){
        String key = RandomStringUtils.random(10, true, true);
        ApiKey apiKeyEntity = ApiKey.builder()
                .key(key)
                .permissions(Arrays.asList("0000", "0001"))
                .status(true)
                .build();

        return apiKeyRepository.save(apiKeyEntity);
    }

    public Shops SignUp(Shops shop) {
                /*
        1. check email
        2. hash password
        3. create publicKey, privateKey
        4. Create shop, keyToken
         */

            boolean holderShop = shopRepository.existsByEmail(shop.getEmail());
            if(holderShop){
                throw new BadRequestError("Error: Shop already in use");
            }

            String passwordHash = hashing.generatePasswordHash(shop.getPassword());

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
            PairKey pairKey = PairKey.createPairKey();

            //save to db, return publicKey like string.
            KeyToken keyToken = KeyToken.builder()
                .shopId(newShop.getId())
                .publicKey( pairKey.getPublicKey())
                .privateKey(pairKey.getPrivateKey())
                .refreshToken(null)
                .build();
             KeyToken saveKey = keyTokenRepository.save(keyToken);

            String publicKeyString = saveKey.getPublicKey();

            if(publicKeyString == null){
                throw new BadRequestError("Error when saving publicKey String");
            }

            return newShop;
    }

    public Map<String, Object> Login(Shops shopEntity) {
        /*
        1. check email
        2. check password
        3. get publicKey, privateKey
        4. Create Tokens
         */

        // check email
        String email = shopEntity.getEmail();
        Shops shop = shopRepository.findByEmail(email);
        if(shop == null){
            throw new BadRequestError("Shop not register");
        }

        // check password
        String passInDB = shop.getPassword();
        if(!hashing.validatePassword(shopEntity.getPassword(), passInDB)){
            throw new AuthFailError("Authentication error");
        }

        // Get public and private key pair
        KeyToken keyToken = keyTokenRepository.findByShopId(shop.getId());

        // Create Token Payload
        Map<String, String> tokenPayload = new Hashtable<>();
        String x = shop.getId();
        tokenPayload.put("shopId", shop.getId());
        tokenPayload.put("email", shop.getEmail());

        //Create accessToken, refreshToken
        Map<String, String> tokens = jsonWebToken.createTokenPair(tokenPayload, keyToken.getPrivateKey());

        keyToken.addToRefreshToken(tokens.get("refreshToken"));

        KeyToken saveKey = keyTokenRepository.save(keyToken);
        // return token
        Map<String, Object> res = new Hashtable<>();
        res.put("shop", shop);
        res.put("tokens", tokens);
        return res;
    }

    public Map<String, Object> HandleRefreshToken(String refreshToken) {
        /*
        1. check RT co trong list used ko?
        2. check RT co dung ko
        3. Neu dung cap lai AT, RT moi
         */

        //1. check RT co trong list used ko?
        KeyToken keyToken = keyTokenRepository.findByRefreshTokenUsed(refreshToken);
       if( keyToken != null){
           // invalid freshtoken
           keyToken.cleanRefreshToken();
           keyTokenRepository.save(keyToken);
           throw new ForbidenError("Something wrong, Please re-login");
       }

       //2. check RT co dung ko
        KeyToken holderToken = keyTokenRepository.findByRefreshToken(refreshToken);
       if(holderToken == null){
           throw new AuthFailError("Shop not register");
       }

       //verify Token
        // check
        Shops shop = jsonWebToken.verifyToken(refreshToken, holderToken.getPublicKey());

        // create 1 cap moi token moi
        // Create Token Payload
        Map<String, String> tokenPayload = new Hashtable<>();
        tokenPayload.put("shopId", shop.getId());
        tokenPayload.put("email", shop.getEmail());
        Map<String, String> tokens = jsonWebToken.createTokenPair(tokenPayload, holderToken.getPrivateKey());

        // update keyToken
        holderToken.addToRefreshTokenUsed(refreshToken);
        holderToken.addToRefreshToken(tokens.get("refreshToken"));
        //save to db, return publickey like string.
        keyTokenRepository.save(holderToken);

        // return token
        Map<String, Object> res = new Hashtable<>();
        res.put("shop", shop);
        res.put("tokens", tokens);
        return res;
    }

    public String Logout(String shopId){
        KeyToken keyToken = keyTokenRepository.findByShopId(shopId);
        keyToken.addToRefreshTokenUsed(keyToken.getRefreshToken());
        keyToken.cleanRefreshToken();
        return keyTokenRepository.save(keyToken).getShopId();
    }
}
