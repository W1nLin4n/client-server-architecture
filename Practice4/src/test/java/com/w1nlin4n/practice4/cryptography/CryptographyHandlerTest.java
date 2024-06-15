package com.w1nlin4n.practice4.cryptography;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class CryptographyHandlerTest {
    private CryptographyHandler cryptographyHandler;

    @BeforeEach
    void setUp() {
        cryptographyHandler = new CryptographyHandler();
    }

    @Test
    void encrypt_decrypt_empty() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] msg = {};
        byte[] encodedMsg = cryptographyHandler.encrypt(msg);
        byte[] decodedMsg = cryptographyHandler.decrypt(encodedMsg);
        assertArrayEquals(msg, decodedMsg);
    }

    @Test
    void encrypt_decrypt_non_empty() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] msg = {0x11, 0x12, 0x13, 0x14};
        byte[] encodedMsg = cryptographyHandler.encrypt(msg);
        byte[] decodedMsg = cryptographyHandler.decrypt(encodedMsg);
        assertArrayEquals(msg, decodedMsg);
    }

    @AfterEach
    void tearDown() {
        cryptographyHandler = null;
    }
}