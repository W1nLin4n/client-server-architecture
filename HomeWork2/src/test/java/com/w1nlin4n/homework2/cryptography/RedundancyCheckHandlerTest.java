package com.w1nlin4n.homework2.cryptography;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedundancyCheckHandlerTest {
    private RedundancyCheckHandler redundancyCheckHandler;

    @BeforeEach
    void setUp() {
        redundancyCheckHandler = new RedundancyCheckHandler();
    }

    @Test
    void encoding_validation_empty() {
        byte[] bytes = {};
        short crc = redundancyCheckHandler.encode(bytes);
        assertEquals((short) 0, crc);
        assertTrue(redundancyCheckHandler.validate(bytes, crc));
    }

    @Test
    void encoding_validation_not_empty() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xAA, 0x45, 0x7A};
        short crc = redundancyCheckHandler.encode(bytes);
        assertEquals((short) 0x47A3, crc);
        assertTrue(redundancyCheckHandler.validate(bytes, crc));
    }

    @AfterEach
    void tearDown() {
        redundancyCheckHandler = null;
    }
}