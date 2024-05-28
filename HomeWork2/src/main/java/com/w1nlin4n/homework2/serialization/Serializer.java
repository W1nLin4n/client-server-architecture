package com.w1nlin4n.homework2.serialization;

public interface Serializer<T> {
    byte[] serialize(T obj);
}
