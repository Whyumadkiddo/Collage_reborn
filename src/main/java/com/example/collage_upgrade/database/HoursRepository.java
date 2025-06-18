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

    // Метод для получения списка групп
    public static ObservableList<String> getGroups() throws SQLException {
        ObservableList<String> groups = FXCollections.observableArrayList();
        String query = "SELECT group_number FROM groups ORDER BY group_number";

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                groups.add(resultSet.getString("group_number"));
            }
        }
        return groups;
    }

    // Метод для получения ID группы по номеру группы
    public static int getGroupId(String groupNumber) throws SQLException {
        if (groupNumber == null || groupNumber.isEmpty()) {
            return -1;
        }

        String query = "SELECT id FROM groups WHERE group_number = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, groupNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        return -1; // Если группа не найдена
    }

    // Метод для сохранения данных в базу с учетом группы
    public static void saveTableData(TableData data, String category, String groupNumber) throws SQLException {
        int groupId = getGroupId(groupNumber);

        String query = "INSERT INTO hours_data (teacher, subject, semester1_hours_week, semester1_hours_plan, " +
                "semester2_hours_week, semester2_hours_plan, total, category, group_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            if (groupId != -1) {
                statement.setInt(9, groupId);
            } else {
                statement.setNull(9, Types.INTEGER);
            }

            statement.executeUpdate();
        }
    }

    // Метод для обновления данных в базе с учетом группы
    public static void updateTableData(TableData data, String category, String groupNumber) throws SQLException {
        int groupId = getGroupId(groupNumber);

        String query = "UPDATE hours_data SET subject = ?, semester1_hours_week = ?, " +
                "semester1_hours_plan = ?, semester2_hours_week = ?, semester2_hours_plan = ?, " +
                "total = ? WHERE teacher = ? AND category = ?";

        if (groupId != -1) {
            query += " AND group_id = ?";
        } else {
            query += " AND group_id IS NULL";
        }

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

            if (groupId != -1) {
                statement.setInt(9, groupId);
            }

            statement.executeUpdate();
        }
    }

    // Метод для удаления данных из базы с учетом группы
    public static void deleteTableData(String teacher, String category, String groupNumber) throws SQLException {
        int groupId = getGroupId(groupNumber);

        String query = "DELETE FROM hours_data WHERE teacher = ? AND category = ?";

        if (groupId != -1) {
            query += " AND group_id = ?";
        } else {
            query += " AND group_id IS NULL";
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, teacher);
            statement.setString(2, category);

            if (groupId != -1) {
                statement.setInt(3, groupId);
            }

            statement.executeUpdate();
        }
    }

    // Метод для загрузки данных из базы для конкретной категории и группы
    public static ObservableList<TableData> loadTableData(String category, String groupNumber) throws SQLException {
        ObservableList<TableData> data = FXCollections.observableArrayList();
        int groupId = getGroupId(groupNumber);

        String query = "SELECT * FROM hours_data WHERE category = ?";

        if (groupId != -1) {
            query += " AND group_id = ?";
        } else {
            query += " AND group_id IS NULL";
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);

            if (groupId != -1) {
                statement.setInt(2, groupId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TableData tableData = new TableData(
                            resultSet.getString("teacher"),
                            resultSet.getString("subject"),
                            "1",
                            resultSet.getString("semester1_hours_week"),
                            resultSet.getString("semester1_hours_plan"),
                            "2",
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
}
