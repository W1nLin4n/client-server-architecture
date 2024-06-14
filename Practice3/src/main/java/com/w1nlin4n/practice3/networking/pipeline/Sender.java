package com.w1nlin4n.practice3.networking.pipeline;

import com.w1nlin4n.practice3.networking.packet.Packet;
import com.w1nlin4n.practice3.serialization.Serializer;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@AllArgsConstructor
public class Sender {
    Serializer<Packet> serializer;

    public void sendPacket(Packet packet, SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(serializer.serialize(packet));
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
}
