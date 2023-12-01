package com.example.shopDev.Models.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document("Product")
public class Product {
    @Id
    private String id;
    @TextIndexed
    private String productName;
    private String productThumb;
    @TextIndexed
    private String productDescription;
    private Double productPrice;
    private Integer productQuantity;
    private String productType;
    private String productSlug;
    private String productShop;
    private Object productAttributes;

    @Builder.Default
    private Double productRatingAverage = 4.5;
    @Builder.Default
    private List<String> productVariations = new ArrayList<>();
    @Builder.Default @Indexed
    private Boolean isDraft = true;
    @Builder.Default @Indexed
    private Boolean isPublished = false;
    @CreatedDate
//    @JsonFormat(shape=JsonFormat.Shape.STRING)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
