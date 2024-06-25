package com.w1nlin4n.practice5.serialization;

import com.w1nlin4n.practice5.cryptography.CryptographyHandler;
import com.w1nlin4n.practice5.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.practice5.networking.message.Message;
import com.w1nlin4n.practice5.networking.packet.Packet;
import com.w1nlin4n.practice5.networking.packet.Packet;
import lombok.AllArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@AllArgsConstructor
public class DefaultPacketSerializer implements Serializer<Packet> {
    protected static final byte magicConst = 0x13;

    private final CryptographyHandler cryptographyHandler;
    private final RedundancyCheckHandler redundancyCheckHandler;

    @Override
    public byte[] serialize(Packet obj) {
        Serializer<Message> messageSerializer = new DefaultMessageSerializer(cryptographyHandler, redundancyCheckHandler);
        byte[] messageBytes = messageSerializer.serialize(obj.getMessage());

        byte[] packetBytes = ByteBuffer.allocate(14).order(ByteOrder.BIG_ENDIAN).put(magicConst).put(obj.getSourceId()).putLong(obj.getPacketId()).putInt(messageBytes.length).array();

        return ByteBuffer.allocate(16 + messageBytes.length).order(ByteOrder.BIG_ENDIAN).put(packetBytes).putShort(redundancyCheckHandler.encode(packetBytes)).put(messageBytes).array();
    }
}
