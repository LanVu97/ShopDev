package com.example.shopDev.Repositories;

import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyTokenRepository extends MongoRepository<KeyToken, String> {


    KeyToken findByRefreshTokenUsed(String refreshToken);

    KeyToken findByShopId(String shopId);

    void deleteByShopId(String shopId);

    KeyToken findByRefreshToken(String refreshToken);


}
