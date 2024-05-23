package com.w1nlin4n.practice1.serialization;

public interface Serializer<T> {
    byte[] serialize(T obj);
}
