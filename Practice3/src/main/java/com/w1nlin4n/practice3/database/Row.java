package com.w1nlin4n.practice3.database;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class Row {
    private final Object id;
    private final HashMap<String, Object> values;
}
