package com.example.shopDev.Exception;

import lombok.Getter;

@Getter
public class ConflictRequestError extends BaseError {

    public ConflictRequestError(String message) {
        super(409, message);
    }
}
