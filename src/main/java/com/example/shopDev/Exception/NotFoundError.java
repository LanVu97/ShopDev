package com.example.shopDev.Exception;

import lombok.Getter;

@Getter
public class NotFoundError extends BaseError {


    public NotFoundError(String message) {
        super(404, message, "error");
    }
}