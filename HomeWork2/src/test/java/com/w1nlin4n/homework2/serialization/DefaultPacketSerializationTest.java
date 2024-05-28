package com.w1nlin4n.homework2.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.w1nlin4n.homework2.cryptography.CryptographyHandler;
import com.w1nlin4n.homework2.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.homework2.dto.ExampleDto;
import com.w1nlin4n.homework2.networking.message.Message;
import com.w1nlin4n.homework2.networking.packet.Packet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPacketSerializationTest {
    private CryptographyHandler cryptographyHandler;
    private RedundancyCheckHandler redundancyCheckHandler;
    private Serializer<Packet> packetSerializer;
    private Deserializer<Packet> packetDeserializer;

    @BeforeEach
    void setUp() {
        cryptographyHandler = new CryptographyHandler();
        redundancyCheckHandler = new RedundancyCheckHandler();
        packetSerializer = new DefaultPacketSerializer(cryptographyHandler, redundancyCheckHandler);
        packetDeserializer = new DefaultPacketDeserializer(cryptographyHandler, redundancyCheckHandler);
    }

    @Test
    void empty_packet_serialization() {
        Packet packet = new Packet((byte) 0, 0L, new Message(0, 0, ""));
        byte[] serialized = packetSerializer.serialize(packet);
        Packet deserialized = packetDeserializer.deserialize(serialized);
        assertEquals(packet, deserialized);
    }

    @Test
    void not_empty_packet_serialization() {
        Packet packet = new Packet((byte) 12, 1023910923L, new Message(23, 993, "Hello World!"));
        byte[] serialized = packetSerializer.serialize(packet);
        Packet deserialized = packetDeserializer.deserialize(serialized);
        assertEquals(packet, deserialized);
    }

    @Test
    void json_packet_serialization() throws JsonProcessingException {
        ExampleDto teacher = new ExampleDto("Vlad", 35, null, null);
        ExampleDto student = new ExampleDto("Ivan", 14, new int[]{12, 10, 11, 12}, teacher);

        Packet packet = new Packet((byte) 12, 1023910923L, new Message(23, 993, student.toJson()));
        byte[] serialized = packetSerializer.serialize(packet);
        Packet deserialized = packetDeserializer.deserialize(serialized);
        assertEquals(packet, deserialized);
        ExampleDto studentDeserialized = ExampleDto.fromJson(deserialized.getMessage().getBody());
        assertEquals(student, studentDeserialized);
    }

    @AfterEach
    void tearDown() {
        cryptographyHandler = null;
        redundancyCheckHandler = null;
        packetSerializer = null;
        packetDeserializer = null;
    }
}