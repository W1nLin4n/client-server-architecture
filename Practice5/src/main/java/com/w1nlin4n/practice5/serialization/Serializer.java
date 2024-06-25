package com.w1nlin4n.practice5.serialization;

public interface Serializer<T> {
    byte[] serialize(T obj);
}
