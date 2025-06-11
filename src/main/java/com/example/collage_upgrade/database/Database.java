package com.example.collage_upgrade.database;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/collage_upgrade";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Unreal2006";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet executeQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return null;
        }
    }

    public static void executeUpdate(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

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
            System.err.println("Error executing prepared update: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    public static boolean tableExists(String tableName) {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, tableName, null)) {
                return tables.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }
}
