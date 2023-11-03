package com.example.shopDev.Services.Product;

import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ClothingRepository;
import com.example.shopDev.Repositories.Product.ElectronicRepository;
import com.example.shopDev.Repositories.Product.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceFactory {

    @Autowired
    ClothingRepository clothingRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ElectronicRepository electronicRepository;

    @Autowired
    ProductRepository productRepository;

    public Product createProduct(String type, Product product, String shopId){
        type = type.toLowerCase();
        ProductCreator productCreator;
        if (type.equals("clothing")) {
            productCreator = new ClothingCreator(productRepository, clothingRepository, objectMapper);

        } else if (type.equals("electronic")) {
            productCreator = new ElectronicCreator(productRepository, electronicRepository, objectMapper);
        }
        else {
            throw new BadRequestError("Invalid product type "+ type);
        }
       return productCreator.createProduct(product, shopId) ;
    }
}
