package com.w1nlin4n.practice3.database;

import com.w1nlin4n.practice3.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void functionalTest() {
        Table table = new Table("table", new HashMap<>());
        Row row = new Row(1, new HashMap<>());
        table.insertRow(row);
        assertEquals(new HashMap<>(), table.getRow(1).getValues());
        HashMap<String, Object> values = new HashMap<>();
        values.put("key", "value");
        Row row2 = new Row(1, values);
        table.updateRow(row2);
        assertEquals(row2.getValues(), table.getRow(1).getValues());
        table.deleteRow(1);
        assertThrows(DatabaseException.class, () -> table.getRow(1));
    }

    @Test
    void insertRowErrorTest() {
        Table table = new Table("table", new HashMap<>());
        Row row = new Row(1, new HashMap<>());
        table.insertRow(row);
        assertThrows(DatabaseException.class, () -> table.insertRow(new Row(1, new HashMap<>())));
    }

    @Test
    void deleteRowErrorTest() {
        Table table = new Table("table", new HashMap<>());
        Row row = new Row(1, new HashMap<>());
        table.insertRow(row);
        table.deleteRow(1);
        assertThrows(DatabaseException.class, () -> table.deleteRow(1));
    }

    @Test
    void getRowErrorTest() {
        Table table = new Table("table", new HashMap<>());
        assertThrows(DatabaseException.class, () -> table.getRow(1));
    }

    @Test
    void updateRowErrorTest() {
        Table table = new Table("table", new HashMap<>());
        assertThrows(DatabaseException.class, () -> table.updateRow(new Row(1, new HashMap<>())));
    }
}