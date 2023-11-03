package com.example.shopDev.Models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@Document("Product")
public class Product {
    @Id
    private String id;
    private String productName;
    private String productThumb;
    private String productDescription;
    private Double productPrice;
    private Integer productQuantity;
    private String productType;
//    private String productShop;
    private Object productAttributes;



}
