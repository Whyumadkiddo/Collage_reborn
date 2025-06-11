package com.example.collage_upgrade.database;

import java.sql.*;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        try (Connection connection = Database.getConnection()) {
            // Создаем таблицу преподавателей с тремя полями
            createTableIfNotExists(connection, "teachers",
                    "CREATE TABLE IF NOT EXISTS teachers (" +
                            "id SERIAL PRIMARY KEY, " +
                            "surname VARCHAR(255) NOT NULL, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "patronymic VARCHAR(255))");

            createTableIfNotExists(connection, "groups",
                    "CREATE TABLE IF NOT EXISTS groups (" +
                            "id SERIAL PRIMARY KEY, " +
                            "group_number VARCHAR(50) NOT NULL)");

            createTableIfNotExists(connection, "subjects",
                    "CREATE TABLE IF NOT EXISTS subjects (" +
                            "id SERIAL PRIMARY KEY, " +
                            "name VARCHAR(255) NOT NULL)");

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void createTableIfNotExists(Connection connection, String tableName, String createTableSQL) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, tableName, null)) {
                if (!tables.next()) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(createTableSQL);
                        System.out.println("Created table: " + tableName);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating table " + tableName + ": " + e.getMessage());
            throw new RuntimeException("Failed to create table " + tableName, e);
        }
    }
}
