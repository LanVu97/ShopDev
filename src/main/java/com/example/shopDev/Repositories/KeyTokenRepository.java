package com.example.shopDev.Repositories;

import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KeyTokenRepository extends MongoRepository<KeyToken, String> {


    KeyToken findByRefreshTokenUsed(String refreshToken);

    KeyToken findByShopId(String shopId);

    void deleteByShopId(String shopId);

    KeyToken findByRefreshToken(String refreshToken);

    KeyToken findByPrivateKey(String s);
}
