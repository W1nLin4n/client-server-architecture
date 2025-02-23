package com.w1nlin4n.practice1.serialization;

import com.w1nlin4n.practice1.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.practice1.exceptions.SerializationException;
import com.w1nlin4n.practice1.networking.message.Message;
import com.w1nlin4n.practice1.networking.packet.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class DefaultPacketDeserializer implements Deserializer<Packet> {
    @Override
    public Packet deserialize(byte[] obj) {
        Deserializer<Message> messageDeserializer = new DefaultMessageDeserializer();

        byte[] packetBytes = Arrays.copyOfRange(obj, 0, 14);
        short crc = ByteBuffer.wrap(Arrays.copyOfRange(obj, 14, 16)).order(ByteOrder.BIG_ENDIAN).getShort();
        byte[] messageBytes = Arrays.copyOfRange(obj, 16, obj.length);

        if(!RedundancyCheckHandler.validate(packetBytes, crc))
            throw new SerializationException("Packet crc check failed", null);

        byte magicConst = ByteBuffer.wrap(Arrays.copyOfRange(packetBytes, 0, 1)).order(ByteOrder.BIG_ENDIAN).get();

        if(magicConst != DefaultPacketSerializer.magicConst)
            throw new SerializationException("Magic const is incorrect", null);

        int packetLength = ByteBuffer.wrap(Arrays.copyOfRange(packetBytes, 10, 14)).order(ByteOrder.BIG_ENDIAN).getInt();

        if(messageBytes.length - 2 != packetLength)
            throw new SerializationException("Incorrect packet length provided", null);

        byte sourceId = ByteBuffer.wrap(Arrays.copyOfRange(packetBytes, 1, 2)).order(ByteOrder.BIG_ENDIAN).get();

        long packetId = ByteBuffer.wrap(Arrays.copyOfRange(packetBytes, 2, 10)).order(ByteOrder.BIG_ENDIAN).getLong();

        Message message = messageDeserializer.deserialize(messageBytes);

        return new Packet(sourceId, packetId, message);
    }
}
