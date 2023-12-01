package com.example.shopDev.DTO;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {

//    private String id;
    private String productName;
    private String productThumb;
    private String productDescription;
    private Double productPrice;
    private Integer productQuantity;
    private String productSlug;
    private String productType;
    //    private String productShop;
    private Object productAttributes;

    private Double productRatingAverage;
    private List<String> productVariations;
    private Boolean isDraft;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
