package com.example.shopDev.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseError extends RuntimeException {
    private int code;
    private String message;
    private String status;


}

