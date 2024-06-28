package com.w1nlin4n.project.database;

import com.w1nlin4n.project.exceptions.DatabaseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class MigrationsManager {
    private final Connection connection;

    public MigrationsManager(Connection connection) {
        this.connection = connection;
        synchronized (this.connection) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS migrations(" +
                                "migration_id VARCHAR(255) PRIMARY KEY" +
                            ");"
                );
                statement.close();
                this.connection.commit();
            } catch (SQLException e) {
                throw new DatabaseException("Could not create migration table", e);
            }
        }
    }

    public void migrate() {
        synchronized (connection) {
            try {
                Set<String> appliedMigrations = getAppliedMigrations();

                for (File file : getAllMigrations()) {
                    if (!appliedMigrations.contains(file.getName()))
                        applyMigration(file);
                }
                connection.commit();
            } catch (SQLException | IOException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not apply migrations", e);
            }
        }
    }

    private Set<String> getAppliedMigrations() throws SQLException {
        Set<String> appliedMigrations = new HashSet<>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM migrations");

        while (results.next()) {
            String migrationId = results.getString("migration_id");
            appliedMigrations.add(migrationId);
        }

        statement.close();
        return appliedMigrations;
    }

    private File[] getAllMigrations() {
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource("migrations");

        try {
            File[] migrationsList = Files
                    .walk(Paths.get(resource.toURI()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toArray(File[]::new);
            return migrationsList;
        } catch (Exception e) {
            return new File[0];
        }
    }

    private void applyMigration(File file) throws SQLException, IOException {
        Statement statement = connection.createStatement();
        if (!file.canRead())
            return;
        String sql = Files.readString(file.toPath());
        statement.executeUpdate(sql);
        statement.close();
        statement = connection.createStatement();
        sql =
                "INSERT INTO migrations (migration_id) " +
                "VALUES ('" + file.getName() + "')";
        statement.executeUpdate(sql);
        statement.close();
    }
}
