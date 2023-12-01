package com.example.shopDev.Models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder()
public class Clothing{
    @Id
    private String id;
    private String brand;
    private String size;
    private String material;
    private String shopId;


}
