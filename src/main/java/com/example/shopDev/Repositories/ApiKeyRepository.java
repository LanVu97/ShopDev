package com.example.shopDev.Repositories;

import com.example.shopDev.Models.ApiKey;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends MongoRepository<ApiKey, String> {


    ApiKey findByKeyAndStatus(String key, boolean b);

}
