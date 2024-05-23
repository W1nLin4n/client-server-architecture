package com.w1nlin4n.practice1.cryptography;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class CryptographyHandlerTest {

    @Test
    void encrypt_decrypt_empty() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] msg = {};
        byte[] encodedMsg = CryptographyHandler.encrypt(msg);
        byte[] decodedMsg = CryptographyHandler.decrypt(encodedMsg);
        assertArrayEquals(msg, decodedMsg);
    }

    @Test
    void encrypt_decrypt_non_empty() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] msg = {0x11, 0x12, 0x13, 0x14};
        byte[] encodedMsg = CryptographyHandler.encrypt(msg);
        byte[] decodedMsg = CryptographyHandler.decrypt(encodedMsg);
        assertArrayEquals(msg, decodedMsg);
    }
}