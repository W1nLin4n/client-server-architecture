package com.w1nlin4n.practice1.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.w1nlin4n.practice1.dto.ExampleDto;
import com.w1nlin4n.practice1.networking.message.Message;
import com.w1nlin4n.practice1.networking.packet.Packet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPacketSerializationTest {
    @Test
    void empty_packet_serialization() {
        Serializer<Packet> serializer = new DefaultPacketSerializer();
        Deserializer<Packet> deserializer = new DefaultPacketDeserializer();
        Packet packet = new Packet((byte) 0, 0L, new Message(0, 0, ""));
        byte[] serialized = serializer.serialize(packet);
        Packet deserialized = deserializer.deserialize(serialized);
        assertEquals(packet, deserialized);
    }

    @Test
    void not_empty_packet_serialization() {
        Serializer<Packet> serializer = new DefaultPacketSerializer();
        Deserializer<Packet> deserializer = new DefaultPacketDeserializer();
        Packet packet = new Packet((byte) 12, 1023910923L, new Message(23, 993, "Hello World!"));
        byte[] serialized = serializer.serialize(packet);
        Packet deserialized = deserializer.deserialize(serialized);
        assertEquals(packet, deserialized);
    }

    @Test
    void json_packet_serialization() throws JsonProcessingException {
        Serializer<Packet> serializer = new DefaultPacketSerializer();
        Deserializer<Packet> deserializer = new DefaultPacketDeserializer();

        ExampleDto teacher = new ExampleDto("Vlad", 35, null, null);
        ExampleDto student = new ExampleDto("Ivan", 14, new int[]{12, 10, 11, 12}, teacher);

        Packet packet = new Packet((byte) 12, 1023910923L, new Message(23, 993, student.toJson()));
        byte[] serialized = serializer.serialize(packet);
        Packet deserialized = deserializer.deserialize(serialized);
        assertEquals(packet, deserialized);
        ExampleDto studentDeserialized = ExampleDto.fromJson(deserialized.getMessage().getBody());
        assertEquals(student, studentDeserialized);
    }
}