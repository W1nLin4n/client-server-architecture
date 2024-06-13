package com.w1nlin4n.practice3.networking.pipeline;

import com.w1nlin4n.practice3.networking.message.Message;
import com.w1nlin4n.practice3.networking.message.MessageCommand;
import com.w1nlin4n.practice3.networking.packet.Packet;
import com.w1nlin4n.practice3.serialization.Deserializer;
import com.w1nlin4n.practice3.serialization.Serializer;
import com.w1nlin4n.practice3.serialization.Deserializer;
import com.w1nlin4n.practice3.serialization.Serializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Receiver {
    private final Deserializer<Packet> deserializer;
    private final Serializer<Packet> serializer;
    private final Handler handler;
    private final Sender sender;

    public void receivePacket(byte[] packetBytes) {
        Packet packet = deserializer.deserialize(packetBytes);
        Message response;
        try {
            response = handler.handleMessage(packet.getMessage());
        } catch (Exception e) {
            response = new Message(MessageCommand.ERROR, packet.getMessage().getUserId(), "");
        }
        byte address = packet.getSourceId();
        Packet responsePacket = new Packet(packet.getSourceId(), packet.getPacketId(), response);
        byte[] responseBytes = serializer.serialize(responsePacket);
        sender.sendPacket(responseBytes, address);
    }
}
