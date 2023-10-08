package com.example.shopDev.Repositories;

import com.example.shopDev.Models.Shops;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;

@Repository
public interface ShopRepository extends MongoRepository<Shops, String> {

    boolean existsByEmail(String email);

    Shops findByEmail(String email);
}
