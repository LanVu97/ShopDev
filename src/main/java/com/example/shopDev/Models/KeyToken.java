package com.example.shopDev.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Builder()
@Document("KeyTokens")
public class KeyToken {
    @Id
    private String id;
    private String shopId;
    private String publicKey;
    private String privateKey;
    private String refreshToken;
    private List<String> refreshTokenUsed;


    public void addToRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void cleanRefreshToken(){
        this.refreshToken = null;
    }

    public boolean addToRefreshTokenUsed(String refreshToken){
        return this.refreshTokenUsed.add(refreshToken);
    }

}
