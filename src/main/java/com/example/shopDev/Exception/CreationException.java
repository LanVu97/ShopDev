package com.example.shopDev.Exception;

public class CreationException extends BaseError {

    public CreationException(String message) {
        super(20003, message, "error");
    }
}