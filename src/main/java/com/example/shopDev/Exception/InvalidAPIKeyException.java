package com.example.shopDev.Exception;

public class InvalidAPIKeyException extends BaseError {

    public InvalidAPIKeyException(String message) {
        super(403, message, "error");
    }
}
