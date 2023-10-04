package com.example.shopDev.Config;

public enum StatusShop {
    ACTIVE("active"), INACTIVE("inactive");

    private final String name;

    StatusShop (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
