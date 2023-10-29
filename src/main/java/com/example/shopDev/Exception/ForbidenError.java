package com.example.shopDev.Exception;

public class ForbidenError extends BaseError {


    public ForbidenError(String message) {
        super(403, message, "error");
    }
}
