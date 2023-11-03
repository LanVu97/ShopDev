package com.example.shopDev.Services.Product;

import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Repositories.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class ProductCreator {

    ProductRepository productRepository;

    public ProductCreator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // method tao product
    public Product createProduct(Product prod, String shopId){
        Product product = Product.builder()
                .productName(prod.getProductName())
                .productThumb(prod.getProductThumb())
                .productDescription(prod.getProductDescription())
                .productPrice(prod.getProductPrice())
                .productQuantity(prod.getProductQuantity())
                .productType(prod.getProductType())
                .productAttributes(prod.getProductAttributes())
                .build();

        return productRepository.save(product);
    }
}
