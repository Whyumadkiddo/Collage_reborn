package com.example.collage_upgrade.database;

import java.sql.*;

public class DatabaseInitializer {
    /**
     * Инициализирует базу данных, создавая необходимые таблицы
     */
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

            System.out.println("База данных успешно инициализирована.");
        } catch (SQLException e) {
            System.err.println("Ошибка инициализации базы данных: " + e.getMessage());
            throw new RuntimeException("Не удалось инициализировать базу данных", e);
        }
    }

    /**
     * Создает таблицу в базе данных, если она не существует
     */
    private static void createTableIfNotExists(Connection connection, String tableName, String createTableSQL) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, tableName, null)) {
                if (!tables.next()) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(createTableSQL);
                        System.out.println("Создана таблица: " + tableName);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы " + tableName + ": " + e.getMessage());
            throw new RuntimeException("Не удалось создать таблицу " + tableName, e);
        }
    }

    /**
     * Обновляет структуру таблицы hours_data, добавляя недостающие столбцы и ограничения
     */
    private static void updateHoursDataTable(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            boolean tableExists = false;
            try (ResultSet tables = metaData.getTables(null, null, "hours_data", null)) {
                tableExists = tables.next();
            }
            if (!tableExists) {
                createHoursDataTable(connection);
                return;
            }

            // Проверяем и добавляем новые столбцы, если их нет
            boolean hasSemester1PracticeColumn = false;
            try (ResultSet columns = metaData.getColumns(null, null, "hours_data", "semester1_hours_practice")) {
                hasSemester1PracticeColumn = columns.next();
            }
            if (!hasSemester1PracticeColumn) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("ALTER TABLE hours_data ADD COLUMN semester1_hours_practice VARCHAR(50)");
                    System.out.println("Добавлен столбец semester1_hours_practice в таблицу hours_data");
                }
            }

            boolean hasSemester2PracticeColumn = false;
            try (ResultSet columns = metaData.getColumns(null, null, "hours_data", "semester2_hours_practice")) {
                hasSemester2PracticeColumn = columns.next();
            }
            if (!hasSemester2PracticeColumn) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("ALTER TABLE hours_data ADD COLUMN semester2_hours_practice VARCHAR(50)");
                    System.out.println("Добавлен столбец semester2_hours_practice в таблицу hours_data");
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении таблицы hours_data: " + e.getMessage());
            throw new RuntimeException("Не удалось обновить таблицу hours_data", e);
        }
    }


    /**
     * Создает таблицу hours_data с полной структурой
     */
    private static void createHoursDataTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE hours_data (" +
                            "id SERIAL PRIMARY KEY, " +
                            "teacher VARCHAR(255) NOT NULL, " +
                            "subject VARCHAR(255) NOT NULL, " +
                            "semester1_hours_week VARCHAR(50), " +
                            "semester1_hours_plan VARCHAR(50), " +
                            "semester1_hours_practice VARCHAR(50), " + // Новое поле для часов по практике в 1 семестре
                            "semester2_hours_week VARCHAR(50), " +
                            "semester2_hours_plan VARCHAR(50), " +
                            "semester2_hours_practice VARCHAR(50), " + // Новое поле для часов по практике во 2 семестре
                            "total VARCHAR(50), " +
                            "category VARCHAR(100), " +
                            "group_id INTEGER REFERENCES groups(id))");
            System.out.println("Создана таблица hours_data с правильной структурой");
        }
    }

}
