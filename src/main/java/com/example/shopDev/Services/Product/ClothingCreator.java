package com.example.shopDev.Services.Product;

import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Exception.CreationException;
import com.example.shopDev.Helper.JsonHelper;
import com.example.shopDev.Helper.ProductHelper;
import com.example.shopDev.Models.product.Clothing;
import com.example.shopDev.Models.product.Electronic;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ClothingRepository;
import com.example.shopDev.Repositories.Product.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

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
            if(productSaved == null){
                throw new CreationException("Create fail");
            }
            Object clothObject =  prod.getProductAttributes();
            String jsonStr = objectMapper.writeValueAsString(clothObject);

            Clothing cloth = objectMapper.readValue(jsonStr, Clothing.class);
            if(cloth == null){
                throw new CreationException("Create fail");
            }
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

    public Product updateProduct(Product prod, Map<String, Object> fields) {

        try {
            // create product
            Product productSaved = super.updateProduct(prod, fields);
            if(productSaved == null){
                throw new CreationException("Create fail");
            }
            Map<String, Object> clothObject = (Map<String, Object>) prod.getProductAttributes();

            Clothing elecById = clothingRepository.findById(prod.getId()).get();
            Clothing cloth = JsonHelper.updateJson(elecById, Clothing.class , clothObject);

            if(cloth == null){
                throw new CreationException("Create fail");
            }
            clothingRepository.save(cloth);
            return productSaved;

        } catch (Exception ex) {
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
            Optional<Clothing> byId = clothingRepository.findById(entity.getId());

            Clothing clothEnity = null;
            if(!byId.isPresent()){
                clothEnity = Clothing.builder().build();
            }else{
                clothEnity = byId.get();
            }

            ProductHelper.patchUpdateProductChild(clothEnity, Clothing.class, productAttributes);
            clothingRepository.save(clothEnity);
        }
        return productEntity;
    }
}
