package com.w1nlin4n.practice4.serialization;

public interface Serializer<T> {
    byte[] serialize(T obj);
}
