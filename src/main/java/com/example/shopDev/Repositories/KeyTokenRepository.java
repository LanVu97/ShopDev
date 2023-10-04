package com.example.shopDev.Repositories;

import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeyTokenRepository extends MongoRepository<KeyToken, String> {


}
