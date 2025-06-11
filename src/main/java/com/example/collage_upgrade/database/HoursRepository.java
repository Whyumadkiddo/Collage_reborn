package com.example.collage_upgrade.database;

import com.example.collage_upgrade.model.TableData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class HoursRepository {
    // Метод для получения списка преподавателей
    public static ObservableList<String> getTeachers() throws SQLException {
        ObservableList<String> teachers = FXCollections.observableArrayList();
        String query = "SELECT surname, name, patronymic FROM teachers ORDER BY surname";

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String surname = resultSet.getString("surname");
                String name = resultSet.getString("name");
                String patronymic = resultSet.getString("patronymic");

                String fullName = surname + " " + name;
                if (patronymic != null && !patronymic.isEmpty()) {
                    fullName += " " + patronymic;
                }
                teachers.add(fullName);
            }
        }
        return teachers;
    }
    public static ObservableList<TableData> loadAllTableData() throws SQLException {
        ObservableList<TableData> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM hours_data";

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                TableData tableData = new TableData(
                        resultSet.getString("teacher"),
                        resultSet.getString("subject"),
                        "1", // Номер семестра 1
                        resultSet.getString("semester1_hours_week"),
                        resultSet.getString("semester1_hours_plan"),
                        "2", // Номер семестра 2
                        resultSet.getString("semester2_hours_week"),
                        resultSet.getString("semester2_hours_plan"),
                        resultSet.getString("total")
                );
                data.add(tableData);
            }
        }
        return data;
    }

    // Метод для получения списка предметов
    public static ObservableList<String> getSubjects() throws SQLException {
        ObservableList<String> subjects = FXCollections.observableArrayList();
        String query = "SELECT name FROM subjects ORDER BY name";

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                subjects.add(resultSet.getString("name"));
            }
        }
        return subjects;
    }

    // Метод для сохранения данных в базу
    public static void saveTableData(TableData data, String category) throws SQLException {
        String query = "INSERT INTO hours_data (teacher, subject, semester1_hours_week, semester1_hours_plan, " +
                "semester2_hours_week, semester2_hours_plan, total, category) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, data.getTeacher());
            statement.setString(2, data.getSubject());
            statement.setString(3, data.getHoursPerWeek1());
            statement.setString(4, data.getHoursByPlan1());
            statement.setString(5, data.getHoursPerWeek2());
            statement.setString(6, data.getHoursByPlan2());
            statement.setString(7, data.getTotal());
            statement.setString(8, category);

            statement.executeUpdate();
        }
    }

    // Метод для обновления данных в базе
    public static void updateTableData(TableData data, String category) throws SQLException {
        String query = "UPDATE hours_data SET subject = ?, semester1_hours_week = ?, " +
                "semester1_hours_plan = ?, semester2_hours_week = ?, semester2_hours_plan = ?, " +
                "total = ? WHERE teacher = ? AND category = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, data.getSubject());
            statement.setString(2, data.getHoursPerWeek1());
            statement.setString(3, data.getHoursByPlan1());
            statement.setString(4, data.getHoursPerWeek2());
            statement.setString(5, data.getHoursByPlan2());
            statement.setString(6, data.getTotal());
            statement.setString(7, data.getTeacher());
            statement.setString(8, category);

            statement.executeUpdate();
        }
    }

    // Метод для удаления данных из базы
    public static void deleteTableData(String teacher, String category) throws SQLException {
        String query = "DELETE FROM hours_data WHERE teacher = ? AND category = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, teacher);
            statement.setString(2, category);

            statement.executeUpdate();
        }
    }

    // Метод для загрузки данных из базы для конкретной категории
    public static ObservableList<TableData> loadTableData(String category) throws SQLException {
        ObservableList<TableData> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM hours_data WHERE category = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TableData tableData = new TableData(
                            resultSet.getString("teacher"),
                            resultSet.getString("subject"),
                            "1", // Номер семестра 1
                            resultSet.getString("semester1_hours_week"),
                            resultSet.getString("semester1_hours_plan"),
                            "2", // Номер семестра 2
                            resultSet.getString("semester2_hours_week"),
                            resultSet.getString("semester2_hours_plan"),
                            resultSet.getString("total")
                    );
                    data.add(tableData);
                }
            }
        }
        return data;
    }

    // Метод для создания таблицы в базе данных (если её нет)
    public static void createHoursTableIfNotExists() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS hours_data (" +
                "id SERIAL PRIMARY KEY, " +
                "teacher VARCHAR(255) NOT NULL, " +
                "subject VARCHAR(255) NOT NULL, " +
                "semester1_hours_week VARCHAR(50), " +
                "semester1_hours_plan VARCHAR(50), " +
                "semester2_hours_week VARCHAR(50), " +
                "semester2_hours_plan VARCHAR(50), " +
                "total VARCHAR(50), " +
                "category VARCHAR(100)" +
                ")";

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }
}
