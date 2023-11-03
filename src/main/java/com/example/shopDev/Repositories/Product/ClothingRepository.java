package com.example.shopDev.Repositories.Product;

import com.example.shopDev.Models.product.Clothing;
import com.example.shopDev.Models.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothingRepository extends MongoRepository<Clothing, String> {

}
