package com.w1nlin4n.practice3.serialization;

import com.w1nlin4n.practice3.cryptography.CryptographyHandler;
import com.w1nlin4n.practice3.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.practice3.exceptions.SerializationException;
import com.w1nlin4n.practice3.networking.message.Message;
import com.w1nlin4n.practice3.networking.message.MessageCommand;
import lombok.AllArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@AllArgsConstructor
public class DefaultMessageDeserializer implements Deserializer<Message> {
    private final CryptographyHandler cryptographyHandler;
    private final RedundancyCheckHandler redundancyCheckHandler;

    @Override
    public Message deserialize(byte[] obj) {
        byte[] messageBytes = Arrays.copyOfRange(obj, 0, obj.length - 2);
        short crc = ByteBuffer.wrap(Arrays.copyOfRange(obj, obj.length - 2, obj.length)).order(ByteOrder.BIG_ENDIAN).getShort();
        if(!redundancyCheckHandler.validate(messageBytes, crc))
            throw new SerializationException("Message crc check failed", null);

        int command = ByteBuffer.wrap(Arrays.copyOfRange(messageBytes, 0, 4)).order(ByteOrder.BIG_ENDIAN).getInt();

        int userId = ByteBuffer.wrap(Arrays.copyOfRange(messageBytes, 4, 8)).order(ByteOrder.BIG_ENDIAN).getInt();

        byte[] bodyBytes = ByteBuffer.wrap(Arrays.copyOfRange(messageBytes, 8, messageBytes.length)).order(ByteOrder.BIG_ENDIAN).array();
        byte[] bodyBytesDecoded;
        try {
            bodyBytesDecoded = cryptographyHandler.decrypt(bodyBytes);
        } catch (Exception e) {
            throw new SerializationException("Deserialization failed during message decryption", e);
        }
        String body = new String(bodyBytesDecoded, StandardCharsets.UTF_8);

        return new Message(MessageCommand.getCommand(command), userId, body);
    }
}
