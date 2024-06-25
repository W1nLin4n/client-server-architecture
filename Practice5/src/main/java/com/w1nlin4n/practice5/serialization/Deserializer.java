package com.w1nlin4n.practice5.serialization;

public interface Deserializer<T> {
    T deserialize(byte[] obj);
}
