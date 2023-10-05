package com.example.shopDev.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.List;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
@Component("ApiKey")
public class ApiKey {
    @Id
    private String id;
    private String key;
    private boolean status;
    private List<String> permissions;


}
