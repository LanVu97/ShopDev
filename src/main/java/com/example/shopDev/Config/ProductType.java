package com.example.shopDev.Config;

public enum ProductType {
    ELECTRONICS("ELECTRONICS"),
    CLOTHING("CLOTHING"),
    FURNITURE("FURNITURE");

    private final String name;

    ProductType (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
