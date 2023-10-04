package com.example.shopDev.Repositories;

import com.example.shopDev.Models.Shops;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends MongoRepository<Shops, String> {

    boolean existsByEmail(String email);
}
