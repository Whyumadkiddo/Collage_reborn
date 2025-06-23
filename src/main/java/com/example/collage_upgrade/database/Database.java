package com.example.collage_upgrade.database;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/collage_upgrade";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Unreal2006";

    /**
     * Устанавливает соединение с базой данных
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Выполняет SQL запрос к базе данных
     */
    public static ResultSet executeQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            return null;
        }
    }

    /**
     * Выполняет SQL запрос на обновление данных в базе
     */
    public static void executeUpdate(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении обновления: " + e.getMessage());
            throw new RuntimeException("Ошибка базы данных: " + e.getMessage());
        }
    }

    /**
     Выполняет подготовленный SQL запрос на обновление данных в базе
     */
    public static void executePreparedUpdate(String query, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    statement.setNull(i + 1, Types.VARCHAR);
                } else {
                    statement.setObject(i + 1, params[i]);
                }
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении подготовленного обновления: " + e.getMessage());
            throw new RuntimeException("Ошибка базы данных: " + e.getMessage());
        }
    }

    /**
     Проверяет существование таблицы в базе данных
     */
    public static boolean tableExists(String tableName) {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, tableName, null)) {
                return tables.next();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при проверке существования таблицы: " + e.getMessage());
            return false;
        }
    }
}
