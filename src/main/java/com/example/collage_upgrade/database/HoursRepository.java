package com.example.collage_upgrade.database;

import com.example.collage_upgrade.model.TableData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class HoursRepository {
    /**
     * Получает список преподавателей из базы данных
     */
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

    /**
     * Получает список предметов из базы данных
     */
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

    /**
     * Получает список групп из базы данных
     */
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

    /**
     * Получает идентификатор группы по ее номеру
     */
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

    /**
     * Сохраняет данные в базу данных с учетом группы
     */
    public static void saveTableData(TableData data, String category, String groupNumber) throws SQLException {
        int groupId = getGroupId(groupNumber);
        String query = "INSERT INTO hours_data (teacher, subject, semester1_hours_week, semester1_hours_plan, semester1_hours_practice, " +
                "semester2_hours_week, semester2_hours_plan, semester2_hours_practice, total, category, group_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, data.getTeacher());
            statement.setString(2, data.getSubject());
            statement.setString(3, data.getHoursPerWeek1());
            statement.setString(4, data.getHoursByPlan1());
            statement.setString(5, data.getHoursByPractice1());
            statement.setString(6, data.getHoursPerWeek2());
            statement.setString(7, data.getHoursByPlan2());
            statement.setString(8, data.getHoursByPractice2());
            statement.setString(9, data.getTotal());
            statement.setString(10, category);
            if (groupId != -1) {
                statement.setInt(11, groupId);
            } else {
                statement.setNull(11, Types.INTEGER);
            }
            statement.executeUpdate();
        }
    }


    /**
     * Обновляет данные в базе данных с учетом группы
     */
    public static void updateTableData(TableData data, String category, String groupNumber) throws SQLException {
        int groupId = getGroupId(groupNumber);
        String query = "UPDATE hours_data SET subject = ?, semester1_hours_week = ?, semester1_hours_plan = ?, semester1_hours_practice = ?, " +
                "semester2_hours_week = ?, semester2_hours_plan = ?, semester2_hours_practice = ?, total = ? " +
                "WHERE teacher = ? AND category = ?";
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
            statement.setString(4, data.getHoursByPractice1());
            statement.setString(5, data.getHoursPerWeek2());
            statement.setString(6, data.getHoursByPlan2());
            statement.setString(7, data.getHoursByPractice2());
            statement.setString(8, data.getTotal());
            statement.setString(9, data.getTeacher());
            statement.setString(10, category);
            if (groupId != -1) {
                statement.setInt(11, groupId);
            }
            statement.executeUpdate();
        }
    }


    /**
     * Удаляет данные из базы данных с учетом группы
     */
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

    /**
     * Загружает данные из базы для конкретной категории и группы
     */
    public static ObservableList<TableData> loadTableData(String category, String groupNumber) throws SQLException {
        ObservableList<TableData> data = FXCollections.observableArrayList();
        int groupId = getGroupId(groupNumber);

        String query = "SELECT h.*, g.group_number FROM hours_data h LEFT JOIN groups g ON h.group_id = g.id WHERE h.category = ?";

        if (groupId != -1) {
            query += " AND h.group_id = ?";
        } else if (groupId == -1 && !groupNumber.isEmpty()) {
            query += " AND h.group_id IS NULL";
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);
            if (groupId != -1) {
                statement.setInt(2, groupId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Выводим имена колонок для отладки
                System.out.println("Колонки в ResultSet:");
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(metaData.getColumnName(i));
                }

                while (resultSet.next()) {
                    // Используем group_number из соединения с таблицей groups
                    String group = resultSet.getString("group_number");

                    // Если group_number null, используем значение по умолчанию
                    if (group == null) {
                        group = "Без группы";
                    }

                    TableData tableData = new TableData(
                            resultSet.getString("teacher"),
                            resultSet.getString("subject"),
                            group, // Используем group_number как номер группы
                            resultSet.getString("semester1_hours_week"),
                            resultSet.getString("semester1_hours_plan"),
                            resultSet.getString("semester1_hours_practice"),
                            group, // Используем тот же group_number для второго семестра
                            resultSet.getString("semester2_hours_week"),
                            resultSet.getString("semester2_hours_plan"),
                            resultSet.getString("semester2_hours_practice"),
                            resultSet.getString("total")
                    );
                    data.add(tableData);
                }
            }
        }
        return data;
    }


    public static ObservableList<TableData> loadAllCategoriesData(String groupNumber) throws SQLException {
        ObservableList<TableData> allData = FXCollections.observableArrayList();

        String[] categories = {
                "Основные",
                "Подгруппы",
                "Подгруппы(программисты)",
                "ПП Спец",
                "УП Спец",
                "Подгруппы(УП)",
                "Часы по учебному плану"
        };

        int groupId = getGroupId(groupNumber);

        for (String category : categories) {
            String query = "SELECT h.*, g.group_number FROM hours_data h LEFT JOIN groups g ON h.group_id = g.id WHERE h.category = ?";

            if (groupId != -1) {
                query += " AND h.group_id = ?";
            } else if (groupId == -1 && !groupNumber.isEmpty()) {
                query += " AND h.group_id IS NULL";
            }

            try (Connection connection = Database.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, category);
                if (groupId != -1) {
                    statement.setInt(2, groupId);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String group = resultSet.getString("group_number");
                        if (group == null) {
                            group = "Без группы";
                        }

                        TableData tableData = new TableData(
                                resultSet.getString("teacher"),
                                resultSet.getString("subject"),
                                group,
                                resultSet.getString("semester1_hours_week"),
                                resultSet.getString("semester1_hours_plan"),
                                resultSet.getString("semester1_hours_practice"),
                                group,
                                resultSet.getString("semester2_hours_week"),
                                resultSet.getString("semester2_hours_plan"),
                                resultSet.getString("semester2_hours_practice"),
                                resultSet.getString("total")
                        );
                        allData.add(tableData);
                    }
                }
            }
        }

        return allData;
    }





}
