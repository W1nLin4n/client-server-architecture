package com.w1nlin4n.practice3.database;

import com.w1nlin4n.practice3.exceptions.DatabaseException;
import com.w1nlin4n.practice3.exceptions.DatabaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class Table {
    private final String tableName;
    private final HashMap<Object, Row> rows;

    public void insertRow(Row row) {
        if(rows.containsKey(row.getId())) {
            throw new DatabaseException("Duplicate primary key", null);
        }
        rows.put(row.getId(), row);
    }

    public void deleteRow(Object id) {
        if(!rows.containsKey(id)) {
            throw new DatabaseException("Primary key not found", null);
        }
        rows.remove(id);
    }

    public Row getRow(Object id) {
        if(!rows.containsKey(id)) {
            throw new DatabaseException("Primary key not found", null);
        }
        return rows.get(id);
    }

    public void updateRow(Row row) {
        if(!rows.containsKey(row.getId())) {
            throw new DatabaseException("Primary key not found", null);
        }
        rows.put(row.getId(), row);
    }
}
