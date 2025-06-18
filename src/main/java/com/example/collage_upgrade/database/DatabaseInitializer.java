package com.example.collage_upgrade.database;

import java.sql.*;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        try (Connection connection = Database.getConnection()) {
            // Создаем таблицу групп
            createTableIfNotExists(connection, "groups",
                    "CREATE TABLE IF NOT EXISTS groups (" +
                            "id SERIAL PRIMARY KEY, " +
                            "group_number VARCHAR(50) NOT NULL UNIQUE)");

            // Создаем таблицу преподавателей
            createTableIfNotExists(connection, "teachers",
                    "CREATE TABLE IF NOT EXISTS teachers (" +
                            "id SERIAL PRIMARY KEY, " +
                            "surname VARCHAR(255) NOT NULL, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "patronymic VARCHAR(255))");

            // Создаем таблицу предметов
            createTableIfNotExists(connection, "subjects",
                    "CREATE TABLE IF NOT EXISTS subjects (" +
                            "id SERIAL PRIMARY KEY, " +
                            "name VARCHAR(255) NOT NULL)");

            // Проверяем и обновляем структуру таблицы hours_data
            updateHoursDataTable(connection);

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

    private static void updateHoursDataTable(Connection connection) {
        try {
            // Проверяем существование таблицы
            DatabaseMetaData metaData = connection.getMetaData();
            boolean tableExists = false;
            try (ResultSet tables = metaData.getTables(null, null, "hours_data", null)) {
                tableExists = tables.next();
            }

            if (!tableExists) {
                // Если таблица не существует, создаем ее
                createHoursDataTable(connection);
                return;
            }

            // Проверяем существование столбца group_id
            boolean hasGroupIdColumn = false;
            try (ResultSet columns = metaData.getColumns(null, null, "hours_data", "group_id")) {
                hasGroupIdColumn = columns.next();
            }

            if (!hasGroupIdColumn) {
                // Если столбец group_id не существует, добавляем его
                try (Statement statement = connection.createStatement()) {
                    statement.execute("ALTER TABLE hours_data ADD COLUMN group_id INTEGER");
                    System.out.println("Added column group_id to hours_data table");
                }
            }

            // Проверяем существование внешнего ключа для group_id
            boolean hasForeignKey = false;
            try (ResultSet foreignKeys = metaData.getImportedKeys(null, null, "hours_data")) {
                while (foreignKeys.next()) {
                    if ("group_id".equals(foreignKeys.getString("FKCOLUMN_NAME"))) {
                        hasForeignKey = true;
                        break;
                    }
                }
            }

            if (!hasForeignKey) {
                // Если внешний ключ не существует, добавляем его
                try (Statement statement = connection.createStatement()) {
                    statement.execute("ALTER TABLE hours_data ADD CONSTRAINT fk_group " +
                            "FOREIGN KEY (group_id) REFERENCES groups(id)");
                    System.out.println("Added foreign key constraint for group_id in hours_data table");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error updating hours_data table: " + e.getMessage());
            throw new RuntimeException("Failed to update hours_data table", e);
        }
    }

    private static void createHoursDataTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE hours_data (" +
                            "id SERIAL PRIMARY KEY, " +
                            "teacher VARCHAR(255) NOT NULL, " +
                            "subject VARCHAR(255) NOT NULL, " +
                            "semester1_hours_week VARCHAR(50), " +
                            "semester1_hours_plan VARCHAR(50), " +
                            "semester2_hours_week VARCHAR(50), " +
                            "semester2_hours_plan VARCHAR(50), " +
                            "total VARCHAR(50), " +
                            "category VARCHAR(100), " +
                            "group_id INTEGER REFERENCES groups(id))"
            );
            System.out.println("Created hours_data table with correct structure");
        }
    }
}
