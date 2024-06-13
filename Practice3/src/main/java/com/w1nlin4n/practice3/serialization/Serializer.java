package com.w1nlin4n.practice3.serialization;

public interface Serializer<T> {
    byte[] serialize(T obj);
}
