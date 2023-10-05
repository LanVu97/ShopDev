package com.example.shopDev.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SuccessResponse {
    private int code;
    private Object metadata;
    private String message;
}
