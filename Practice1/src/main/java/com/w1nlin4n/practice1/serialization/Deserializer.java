package com.w1nlin4n.practice1.serialization;

public interface Deserializer<T> {
    T deserialize(byte[] obj);
}
