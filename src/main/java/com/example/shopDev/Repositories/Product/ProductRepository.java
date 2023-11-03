package com.example.shopDev.Repositories.Product;


import com.example.shopDev.Models.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {



}
