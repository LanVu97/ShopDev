package com.example.shopDev.DTO;

import lombok.Data;

@Data
public class ProductDTO {

//    private String id;
    private String productName;
    private String productThumb;
    private String productDescription;
    private Double productPrice;
    private Integer productQuantity;
    private String productType;
    //    private String productShop;
    private Object productAttributes;
}
