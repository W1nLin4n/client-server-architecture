package com.w1nlin4n.practice1.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.w1nlin4n.practice1.dto.ExampleDto;
import com.w1nlin4n.practice1.networking.message.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMessageSerializationTest {
    @Test
    void empty_message_serialization() {
        Serializer<Message> serializer = new DefaultMessageSerializer();
        Deserializer<Message> deserializer = new DefaultMessageDeserializer();
        Message message = new Message(0, 0, "");
        byte[] serialized = serializer.serialize(message);
        Message deserialized = deserializer.deserialize(serialized);
        assertEquals(message, deserialized);
    }

    @Test
    void not_empty_message_serialization() {
        Serializer<Message> serializer = new DefaultMessageSerializer();
        Deserializer<Message> deserializer = new DefaultMessageDeserializer();
        Message message = new Message(23, 993, "Hello World!");
        byte[] serialized = serializer.serialize(message);
        Message deserialized = deserializer.deserialize(serialized);
        assertEquals(message, deserialized);
    }

    @Test
    void json_message_serialization() throws JsonProcessingException {
        Serializer<Message> serializer = new DefaultMessageSerializer();
        Deserializer<Message> deserializer = new DefaultMessageDeserializer();

        ExampleDto teacher = new ExampleDto("Vlad", 35, null, null);
        ExampleDto student = new ExampleDto("Ivan", 14, new int[]{12, 10, 11, 12}, teacher);

        Message message = new Message(23, 993, student.toJson());
        byte[] serialized = serializer.serialize(message);
        Message deserialized = deserializer.deserialize(serialized);
        assertEquals(message, deserialized);
        ExampleDto studentDeserialized = ExampleDto.fromJson(message.getBody());
        assertEquals(student, studentDeserialized);
    }
}