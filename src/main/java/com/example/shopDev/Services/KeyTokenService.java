package com.example.shopDev.Services;

import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Exception.NotFoundError;
import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.KeyTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class KeyTokenService {

    @Autowired
    KeyTokenRepository keyTokenRepository;

    public String createKeyToken(Shops shop, String publicKey, String privateKey) {

        KeyToken keyToken = KeyToken.builder()
                .shopId(shop.getId())
                .publicKey(publicKey)
                .privateKey(privateKey)
                .build();
        // save to db

        KeyToken token =  keyTokenRepository.save(keyToken);
        return token.getPublicKey();
    }

    public String createKeyToken(Shops shop, String publicKey, String privateKey, String newRefreshToken) {

        KeyToken keys = keyTokenRepository.findByShopId(shop.getId());
        List<String> refreshTokenUseds = new ArrayList<>();
        KeyToken keyToken = KeyToken.builder()
                .shopId(shop.getId())
                .publicKey(publicKey)
                .refreshTokenUsed(refreshTokenUseds)
                .privateKey(privateKey).build();

        if(keys != null){
            String refreshTokenUsed = keys.getRefreshToken();
            refreshTokenUseds = keys.getRefreshTokenUsed();

            refreshTokenUseds.add(refreshTokenUsed);

            keyToken = keyToken.toBuilder()
                    .id(keys.getId())
                    .refreshTokenUsed(refreshTokenUseds)
                    .build();
        }

        keyToken = keyToken.toBuilder()
                .refreshToken(newRefreshToken)
                .build();

        // save to db
        KeyToken token =  keyTokenRepository.save(keyToken);
        return token.getPublicKey();
    }

    public boolean isInUsed(String refreshToken){
        KeyToken key = keyTokenRepository.findByRefreshTokenUsed(refreshToken);

                if( key != null){
                    return true;
                }
                return false;
    }

    public void deleteByRefreshTokenUsed(String refreshToken){
        KeyToken keyToken = keyTokenRepository.findByRefreshTokenUsed(refreshToken);
        keyTokenRepository.deleteByShopId(keyToken.getShopId());
    }

    public KeyToken findByRefreshTokenUsed(String refreshToken){
        return  keyTokenRepository.findByRefreshTokenUsed(refreshToken);
    }

    public KeyToken findByRefreshToken(String refreshToken){
        return  keyTokenRepository.findByRefreshToken(refreshToken);
    }

    public void updateKeyToken(Shops shop, String oldRefreshToken, String newRefreshToken){
        KeyToken keys = keyTokenRepository.findByShopId(shop.getId());
        List<String> refreshTokenUseds = keys.getRefreshTokenUsed();
        refreshTokenUseds.add(oldRefreshToken);
        KeyToken keyToken = KeyToken.builder()
                .id(keys.getId())
                .refreshToken(newRefreshToken)
                .shopId(shop.getId())
                .refreshTokenUsed(refreshTokenUseds)
                .build();

        // save to db
        KeyToken token =  keyTokenRepository.save(keyToken);

    }
}
