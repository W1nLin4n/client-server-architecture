package com.w1nlin4n.practice1.cryptography;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedundancyCheckHandlerTest {
    @Test
    void encoding_validation_empty() {
        byte[] bytes = {};
        short crc = RedundancyCheckHandler.encode(bytes);
        assertEquals((short) 0, crc);
        assertTrue(RedundancyCheckHandler.validate(bytes, crc));
    }

    @Test
    void encoding_validation_not_empty() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xAA, 0x45, 0x7A};
        short crc = RedundancyCheckHandler.encode(bytes);
        assertEquals((short) 0x47A3, crc);
        assertTrue(RedundancyCheckHandler.validate(bytes, crc));
    }
}