package com.example.shopDev.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Data
public class ErrorObject {
    private int code;
    private String status;
    private String message;
}
