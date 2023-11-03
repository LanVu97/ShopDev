package com.example.shopDev.Models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @NoArgsConstructor @AllArgsConstructor
@Builder()
public class Electronic {
    @Id
    private String id;
    private String manufacturer;
    private String model;
    private String color;
    private String shopId;
}
