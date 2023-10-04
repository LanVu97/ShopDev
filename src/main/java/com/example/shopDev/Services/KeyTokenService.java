package com.example.shopDev.Services;

import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
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
import java.util.Base64;
import java.util.List;

@Service
public class KeyTokenService {

    @Autowired
    KeyTokenRepository keyTokenRepository;

    public String createKeyToken(Shops shop, RSAPublicKey publicKey) throws NoSuchAlgorithmException {

        String pubkey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        KeyToken keyToken = new KeyToken(shop.getId(), pubkey);

        KeyToken keyToken = KeyToken.builder()
                .shopId(shop.getId())
                .publicKey(pubkey)
                .build();
        // save to db

        KeyToken token =  keyTokenRepository.save(keyToken);
        return token.getPublicKey();
    }
}
