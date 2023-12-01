package com.example.shopDev.Services.Product;

import com.example.shopDev.Helper.JsonHelper;
import com.example.shopDev.Helper.ProductHelper;
import com.example.shopDev.Helper.Utils;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


public class ProductCreator {

    ProductRepository productRepository;

    public ProductCreator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // method tao product
    public Product createProduct(Product prod, String shopId){

        String productSlug = Utils.toSlug(prod.getProductName());

        Product product = Product.builder()
                .productName(prod.getProductName())
                .productThumb(prod.getProductThumb())
                .productDescription(prod.getProductDescription())
                .productPrice(prod.getProductPrice())
                .productQuantity(prod.getProductQuantity())
                .productType(prod.getProductType())
                .productShop(shopId)
                .productSlug(productSlug)
                .productAttributes(prod.getProductAttributes())
                .build();

        return productRepository.save(product);
    }

    public Product updateProduct(Product product, Map<String, Object> fields){
        Product updateProduct = JsonHelper.updateJson(product,Product.class, fields);
        return productRepository.save(updateProduct);
    }


    public Product patchUpdateProduct(Product entity, Map<String, Object> data){
        ProductHelper.patchUpdateProduct(entity, data);
        return productRepository.save(entity);
    }
}
