package com.example.shopDev.Services.Product;

import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Models.product.Clothing;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ClothingRepository;
import com.example.shopDev.Repositories.Product.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ClothingCreator extends ProductCreator {
    ClothingRepository clothingRepository;
    ObjectMapper objectMapper;


    public ClothingCreator(ProductRepository productRepository, ClothingRepository clothingRepository, ObjectMapper objectMapper) {
        super(productRepository);
        this.clothingRepository = clothingRepository;
        this.objectMapper = objectMapper;
    }

    // method tao product
    public Product createProduct(Product prod, String shopId) {

        try {
            // create product
            Product productSaved = super.createProduct(prod, shopId);
            Object clothObject =  prod.getProductAttributes();
//           Map<String, String> map = (Map<String, String>) clotht;
            String jsonStr = objectMapper.writeValueAsString(clothObject);

            Clothing cloth = objectMapper.readValue(jsonStr, Clothing.class);

            Clothing clothing = Clothing.builder()
                    .id(productSaved.getId())
                    .brand(cloth.getBrand())
                    .size(cloth.getSize())
                    .material(cloth.getMaterial())
                    .shopId(shopId)
                    .build();

            clothingRepository.save(clothing);
            return productSaved;

        } catch (JsonProcessingException ex) {
            throw new BadRequestError("error" + ex);
        }


    }
}
