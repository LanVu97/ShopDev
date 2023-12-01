package com.example.shopDev.Services.Product;

import com.example.shopDev.Config.ProductType;
import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Exception.NotFoundError;
import com.example.shopDev.Helper.JsonHelper;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ClothingRepository;
import com.example.shopDev.Repositories.Product.ElectronicRepository;
import com.example.shopDev.Repositories.Product.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.shopDev.Config.ProductType.CLOTHING;

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

    public ProductCreator getProductObject(String type){
        type = type.toUpperCase();
        ProductCreator productCreator ;
        ProductType productType = ProductType.valueOf(type);

        switch (productType){
            case CLOTHING:
                productCreator = new ClothingCreator(productRepository, clothingRepository, objectMapper);
                break;
            case ELECTRONICS:
                productCreator = new ElectronicCreator(productRepository, electronicRepository, objectMapper);
                break;
            default:
                throw new BadRequestError("Invalid product type "+ type);
        }
        return productCreator;
    }

    public Product createProduct(String type, Product product, String shopId){
        try{
            ProductCreator productCreator  = getProductObject(type);
            return productCreator.createProduct(product, shopId) ;

        }catch (Exception ex){
            throw new BadRequestError("Invalid product type "+ type);
        }

    }

    public void updateProduct(String product_id, Map<String, Object> fields, String shopId){

        Product product = productRepository.findById(product_id).orElseThrow(() -> new NotFoundError("not found product"));

        if(!shopId.equals(product.getProductShop())){
            throw new BadRequestError("Insufficient Permisison");
        }

        // update sub product
        String type = product.getProductType();
        ProductCreator productCreator = getProductObject(type);
        productCreator.updateProduct(product, fields) ;
    }



    public List<Product> GetAllDraftForShop( String shopId, int size, int page){
/*
    @desc Get all drafts for shop

 */
        Pageable paging = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Product> pageTuts = productRepository.findByIsDraftAndProductShop(true, shopId, paging);
        return pageTuts.getContent();
    }

    public List<Product> GetAllPublishForShop( String shopId, int size, int page){
    /*
        @desc Get all published for shop

     */
        Pageable paging = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Product> pageTuts = productRepository.findByIsPublishedAndProductShop(true, shopId, paging);
        return pageTuts.getContent();
    }


    public Product publishProductByShop(String product_shop, String product_id){

        Product product = productRepository.findByProductShopAndId(product_shop,product_id);

        if(product == null){
            return null;
        }

        product.setIsDraft(false);
        product.setIsPublished(true);
        return productRepository.save(product);
    }

    public Product unPublishProductByShop(String product_shop, String product_id) {
        Product product = productRepository.findByProductShopAndId(product_shop,product_id);

        if(product == null){
            return null;
        }

        product.setIsDraft(true);
        product.setIsPublished(false);
        return productRepository.save(product);
    }

    public List<Product> searchProductByUser(String keySearch, int size, int page){
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matchingAny(keySearch);

        return productRepository.findByIsPublished(criteria, true);

    }

    public List<Product> findAllProducts(int size, int page){


        Pageable paging = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Product> pageTuts = productRepository.findByIsPublished(paging, true);
        return pageTuts.getContent();
    }

    public Product findProductDetail(String product_id){

        return productRepository.findByIdAndIsPublished(product_id, true);
    }

    public Product patchUpdate(String shopId, String productId, Map<String, Object> data) {
        Optional<Product> byId = productRepository.findById(productId);
        if(!byId.isPresent()){
            throw new NotFoundError("Product not found. Id=" + productId);
        }
        Product entity = byId.get();
        if(!shopId.equals(entity.getProductShop())){
            throw new BadRequestError("Insufficient Permisison");
        }

        ProductCreator factory = getProductObject(entity.getProductType());
        Product afterUpdate = factory.patchUpdateProduct(entity, data);

        return afterUpdate;
    }
}
