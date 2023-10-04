package com.example.shopDev.Models;

import com.example.shopDev.Config.StatusShop;
import lombok.*;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

@Data @NoArgsConstructor @AllArgsConstructor
@Builder
@Document("shops")
public class Shops {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;

    private List<String> roles;
    private boolean isVerify;
    private String status;

}
