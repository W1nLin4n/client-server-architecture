package com.w1nlin4n.homework2.database;

import com.w1nlin4n.homework2.exceptions.DatabaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class Database {
    private final String name;
    private final HashMap<String, Table> tables;

    public void createTable(String tableName) {
        if (tables.containsKey(tableName)) {
            throw new DatabaseException("Table " + tableName + " already exists", null);
        }
        Table table = new Table(tableName, new HashMap<>());
        tables.put(tableName, table);
    }

    public void dropTable(String tableName) {
        if (!tables.containsKey(tableName)) {
            throw new DatabaseException("Table " + tableName + " does not exist", null);
        }
        tables.remove(tableName);
    }

    public Table getTable(String tableName) {
        if (!tables.containsKey(tableName)) {
            throw new DatabaseException("Table " + tableName + " does not exist", null);
        }
        return tables.get(tableName);
    }
}
