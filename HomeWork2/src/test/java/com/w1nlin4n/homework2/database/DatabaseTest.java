package com.w1nlin4n.homework2.database;

import com.w1nlin4n.homework2.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    @Test
    void functionalTest() {
        Database database = new Database("database", new HashMap<>());
        database.createTable("table");
        Table table = database.getTable("table");
        assertEquals("table", table.getTableName());
        database.dropTable("table");
        assertThrows(DatabaseException.class, () -> database.getTable("table"));
    }

    @Test
    void createTableErrorTest() {
        Database database = new Database("database", new HashMap<>());
        database.createTable("table");
        assertThrows(DatabaseException.class, () -> database.createTable("table"));
    }

    @Test
    void dropTableErrorTest() {
        Database database = new Database("database", new HashMap<>());
        assertThrows(DatabaseException.class, () -> database.dropTable("table"));
    }

    @Test
    void getTableErrorTest() {
        Database database = new Database("database", new HashMap<>());
        assertThrows(DatabaseException.class, () -> database.getTable("table"));
    }
}