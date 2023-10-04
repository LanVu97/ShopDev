package com.example.shopDev.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Builder
@Document("KeyTokens")
public class KeyToken {
    @Id
    private String id;
    private String shopId;
    private String publicKey;
    private List<String> refreshToken;



    public String getId() {
        return id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public KeyToken(String shopId, String publicKey) {
        this.shopId = shopId;
        this.publicKey = publicKey;
    }
}
