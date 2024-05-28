package com.w1nlin4n.homework2.serialization;

import com.w1nlin4n.homework2.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.homework2.networking.message.Message;
import com.w1nlin4n.homework2.networking.packet.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DefaultPacketSerializer implements Serializer<Packet> {
    protected static byte magicConst = 0x13;

    @Override
    public byte[] serialize(Packet obj) {
        Serializer<Message> messageSerializer = new DefaultMessageSerializer();
        byte[] messageBytes = messageSerializer.serialize(obj.getMessage());

        byte[] packetBytes = ByteBuffer.allocate(14).order(ByteOrder.BIG_ENDIAN).put(magicConst).put(obj.getSourceId()).putLong(obj.getPacketId()).putInt(messageBytes.length - 2).array();

        return ByteBuffer.allocate(16 + messageBytes.length).order(ByteOrder.BIG_ENDIAN).put(packetBytes).putShort(RedundancyCheckHandler.encode(packetBytes)).put(messageBytes).array();

    }
}
