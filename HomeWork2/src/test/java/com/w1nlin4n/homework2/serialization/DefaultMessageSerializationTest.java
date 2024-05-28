package com.w1nlin4n.homework2.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.homework2.cryptography.CryptographyHandler;
import com.w1nlin4n.homework2.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.homework2.dto.ExampleDto;
import com.w1nlin4n.homework2.networking.message.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMessageSerializationTest {
    private CryptographyHandler cryptographyHandler;
    private RedundancyCheckHandler redundancyCheckHandler;
    private Serializer<Message> messageSerializer;
    private Deserializer<Message> messageDeserializer;

    @BeforeEach
    void setUp() {
        cryptographyHandler = new CryptographyHandler();
        redundancyCheckHandler = new RedundancyCheckHandler();
        messageSerializer = new DefaultMessageSerializer(cryptographyHandler, redundancyCheckHandler);
        messageDeserializer = new DefaultMessageDeserializer(cryptographyHandler, redundancyCheckHandler);
    }

    @Test
    void empty_message_serialization() {
        Message message = new Message(0, 0, "");
        byte[] serialized = messageSerializer.serialize(message);
        Message deserialized = messageDeserializer.deserialize(serialized);
        assertEquals(message, deserialized);
    }

    @Test
    void not_empty_message_serialization() {
        Message message = new Message(23, 993, "Hello World!");
        byte[] serialized = messageSerializer.serialize(message);
        Message deserialized = messageDeserializer.deserialize(serialized);
        assertEquals(message, deserialized);
    }

    @Test
    void json_message_serialization() throws JsonProcessingException {
        ExampleDto teacher = new ExampleDto("Vlad", 35, null, null);
        ExampleDto student = new ExampleDto("Ivan", 14, new int[]{12, 10, 11, 12}, teacher);

        Message message = new Message(23, 993, student.toJson());
        byte[] serialized = messageSerializer.serialize(message);
        Message deserialized = messageDeserializer.deserialize(serialized);
        assertEquals(message, deserialized);
        ExampleDto studentDeserialized = ExampleDto.fromJson(message.getBody());
        assertEquals(student, studentDeserialized);
    }

    @AfterEach
    void tearDown() {
        cryptographyHandler = null;
        redundancyCheckHandler = null;
        messageSerializer = null;
        messageDeserializer = null;
    }
}