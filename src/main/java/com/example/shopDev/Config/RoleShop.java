package com.example.shopDev.Config;

public enum RoleShop {
    SHOP("SHOP"),
    EDITOR("EDITOR"),
    ADMIN("ADMIN"),
    WRITER("WRITER");

    private final String name;

    RoleShop (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
