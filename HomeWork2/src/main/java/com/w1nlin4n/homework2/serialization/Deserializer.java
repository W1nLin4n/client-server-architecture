package com.w1nlin4n.homework2.serialization;

public interface Deserializer<T> {
    T deserialize(byte[] obj);
}
