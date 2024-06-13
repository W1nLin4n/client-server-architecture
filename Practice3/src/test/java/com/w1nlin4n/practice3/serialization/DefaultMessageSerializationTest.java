package com.w1nlin4n.practice3.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.w1nlin4n.practice3.cryptography.CryptographyHandler;
import com.w1nlin4n.practice3.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.practice3.dto.ExampleDto;
import com.w1nlin4n.practice3.networking.message.Message;
import com.w1nlin4n.practice3.networking.message.MessageCommand;
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
        Message message = new Message(MessageCommand.ERROR, 0, "");
        byte[] serialized = messageSerializer.serialize(message);
        Message deserialized = messageDeserializer.deserialize(serialized);
        assertEquals(message, deserialized);
    }

    @Test
    void not_empty_message_serialization() {
        Message message = new Message(MessageCommand.INFORMATION, 993, "Hello World!");
        byte[] serialized = messageSerializer.serialize(message);
        Message deserialized = messageDeserializer.deserialize(serialized);
        assertEquals(message, deserialized);
    }

    @Test
    void json_message_serialization() throws JsonProcessingException {
        ExampleDto teacher = new ExampleDto("Vlad", 35, null, null);
        ExampleDto student = new ExampleDto("Ivan", 14, new int[]{12, 10, 11, 12}, teacher);

        Message message = new Message(MessageCommand.INFORMATION, 993, student.toJson());
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