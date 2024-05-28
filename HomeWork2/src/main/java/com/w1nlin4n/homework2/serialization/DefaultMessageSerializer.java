package com.w1nlin4n.homework2.serialization;

import com.w1nlin4n.homework2.cryptography.CryptographyHandler;
import com.w1nlin4n.homework2.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.homework2.exceptions.SerializationException;
import com.w1nlin4n.homework2.networking.message.Message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class DefaultMessageSerializer implements Serializer<Message> {
    @Override
    public byte[] serialize(Message obj) {
        byte[] bodyBytes = obj.getBody().getBytes(StandardCharsets.UTF_8);
        try {
            bodyBytes = CryptographyHandler.encrypt(bodyBytes);
        } catch (Exception e) {
            throw new SerializationException("Serialization failed during message encryption", e);
        }

        byte[] messageBytes = ByteBuffer.allocate(8 + bodyBytes.length).order(ByteOrder.BIG_ENDIAN).putInt(obj.getCommand()).putInt(obj.getUserId()).put(bodyBytes).array();
        return ByteBuffer.allocate(messageBytes.length + 2).order(ByteOrder.BIG_ENDIAN).put(messageBytes).putShort(RedundancyCheckHandler.encode(messageBytes)).array();
    }
}
