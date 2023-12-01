package com.example.shopDev.Services.Product;

import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Exception.NotFoundError;
import com.example.shopDev.Helper.JsonHelper;
import com.example.shopDev.Helper.ProductHelper;
import com.example.shopDev.Models.product.Clothing;
import com.example.shopDev.Models.product.Electronic;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ElectronicRepository;
import com.example.shopDev.Repositories.Product.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

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
//
    public Product updateProduct(Product prod, Map<String, Object> fields){
        try{
            // create product
            Product productSaved =  super.updateProduct(prod, fields);

            Map<String, Object> electricObject = (Map<String, Object>) prod.getProductAttributes();

            Electronic elecById = electronicRepository.findById(prod.getId()).orElseThrow(() ->{
                throw new NotFoundError("Not found electric product");
            });

            Electronic elect = JsonHelper.updateJson(elecById, Electronic.class , electricObject);

            electronicRepository.save(elect);
            return productSaved;

        }catch (Exception ex) {
            throw new BadRequestError("error" + ex);
        }
    }

    public Product patchUpdateProduct(Product entity, Map<String, Object> data){
        Product productEntity = super.patchUpdateProduct(entity, data);

        if(productEntity == null){
            throw new BadRequestError("Update fail");
        }

        if(data.get("productAttributes") != null){
            Map<String, Object> productAttributes = (Map<String, Object>) data.get("productAttributes");

            Optional<Electronic> byId = electronicRepository.findById(entity.getId());

            Electronic electricEnity = null;
            if(!byId.isPresent()){
                electricEnity = Electronic.builder().build();
            }else{
                electricEnity = byId.get();
            }

            ProductHelper.patchUpdateProductChild(electricEnity, Electronic.class, productAttributes);
            electronicRepository.save(electricEnity);
        }
        return productEntity;
    }

}