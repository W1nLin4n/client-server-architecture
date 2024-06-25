package com.w1nlin4n.practice5.networking.pipeline;

import com.w1nlin4n.practice5.networking.packet.Packet;
import com.w1nlin4n.practice5.serialization.Deserializer;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

@AllArgsConstructor
public class Receiver {
    private final Deserializer<Packet> deserializer;

    public Packet receivePacket(SocketChannel channel) throws IOException {
        ByteBuffer packetBuffer = ByteBuffer.allocate(16);
        while (packetBuffer.hasRemaining()) {
            channel.read(packetBuffer);
        }
        packetBuffer.flip();

        int messageLength = ByteBuffer.wrap(Arrays.copyOfRange(packetBuffer.array(), 10, 14)).order(ByteOrder.BIG_ENDIAN).getInt();

        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
        while (messageBuffer.hasRemaining()) {
            channel.read(messageBuffer);
        }
        messageBuffer.flip();

        byte[] packetBytes = ByteBuffer.allocate(messageLength + 16).put(packetBuffer).put(messageBuffer).array();

        return deserializer.deserialize(packetBytes);
    }

}
