package com.w1nlin4n.practice4.serialization;

public interface Deserializer<T> {
    T deserialize(byte[] obj);
}
