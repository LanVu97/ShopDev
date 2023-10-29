package com.example.shopDev;

import com.example.shopDev.Security.Hashing;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordHashingUtilsTest {

    private Hashing passwordHashing = new Hashing();

    @Test
    public void checkPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String rawPassword = "dfsdfsd";
        String hashPassword = passwordHashing.generatePasswordHash(rawPassword);

        //verify
        boolean verify = passwordHashing.validatePassword(rawPassword, hashPassword);

        assert verify == true;

    }
}
