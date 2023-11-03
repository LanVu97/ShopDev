package com.example.shopDev.Services.Product;

import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Models.product.Electronic;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ElectronicRepository;
import com.example.shopDev.Repositories.Product.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class ElectronicCreator extends ProductCreator {
    ElectronicRepository electronicRepository;
    ObjectMapper objectMapper;

    public ElectronicCreator(ProductRepository productRepository, ElectronicRepository electronicRepository, ObjectMapper objectMapper) {
        super(productRepository);
        this.electronicRepository = electronicRepository;
        this.objectMapper = objectMapper;
    }

    // method tao product
    public Product createProduct(Product prod, String shopId){

        try{
            // create product
            Product productSaved =  super.createProduct(prod, shopId);

            Object electricObject =  prod.getProductAttributes();
            String jsonStr = objectMapper.writeValueAsString(electricObject);

            Electronic electronic = objectMapper.readValue(jsonStr, Electronic.class);

            Electronic elect = Electronic.builder()
                    .id(productSaved.getId())
                    .manufacturer(electronic.getManufacturer())
                    .model(electronic.getModel())
                    .color(electronic.getColor())
                    .shopId(shopId)
                    .build();

            electronicRepository.save(elect);
            return productSaved;

        }catch (JsonProcessingException ex) {
            throw new BadRequestError("error" + ex);
        }


    }

}