package com.example.shopDev.Exception;

import lombok.Getter;

@Getter
public class BadRequestError extends BaseError {


    public BadRequestError(String message) {
        super(400, message, "error");
    }
}
