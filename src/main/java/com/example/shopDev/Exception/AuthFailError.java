package com.example.shopDev.Exception;

import lombok.Getter;

@Getter
public class AuthFailError  extends BaseError {


    public AuthFailError(String message) {
        super(401, message, "error");
    }
}
