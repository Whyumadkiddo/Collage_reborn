package com.example.collage_upgrade.other;

import com.example.collage_upgrade.database.Database;
import com.example.collage_upgrade.database.DatabaseInitializer;
import com.example.collage_upgrade.util.DialogUtils;
import com.example.collage_upgrade.util.PaneUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ElementsController {
    @FXML private Pane groupOuterPane;
    @FXML private Pane groupInnerPane;
    @FXML private Pane teachersOuterPane;
    @FXML private Pane teachersInnerPane;
    @FXML private Pane subjectsOuterPane;
    @FXML private Pane subjectsInnerPane;
    @FXML private Text categoryText;
    @FXML private Pane addButton;
    @FXML private Pane deleteButton;

    // Таблица для групп и предметов (одна колонка)
    @FXML private TableView<SimpleItem> simpleTableView;
    @FXML private TableColumn<SimpleItem, String> simpleDataColumn;

    // Таблица для преподавателей (три колонки)
    @FXML private TableView<Teacher> teachersTableView;
    @FXML private TableColumn<Teacher, String> surnameColumn;
    @FXML private TableColumn<Teacher, String> nameColumn;
    @FXML private TableColumn<Teacher, String> patronymicColumn;

    private String currentCategory = "Преподаватель";
    private String currentTable = "teachers";

    /**
     * Инициализация контроллера, настройка таблиц и обработчиков событий
     */
    @FXML
    public void initialize() {
        DatabaseInitializer.initializeDatabase();

        PaneUtils.applyRoundedStyle(groupOuterPane);
        PaneUtils.applyRoundedStyle(groupInnerPane);
        PaneUtils.applyRoundedStyle(teachersOuterPane);
        PaneUtils.applyRoundedStyle(teachersInnerPane);
        PaneUtils.applyRoundedStyle(subjectsOuterPane);
        PaneUtils.applyRoundedStyle(subjectsInnerPane);
        PaneUtils.applyRoundedStyle(addButton);
        PaneUtils.applyRoundedStyle(deleteButton);

        // Настройка простой таблицы (для групп и предметов)
        simpleDataColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        simpleDataColumn.setCellFactory(getCenteredSimpleCellFactory());

        // Настройка таблицы преподавателей
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));

        // Устанавливаем стиль для колонок
        surnameColumn.setCellFactory(getCenteredTeacherCellFactory());
        nameColumn.setCellFactory(getCenteredTeacherCellFactory());
        patronymicColumn.setCellFactory(getCenteredTeacherCellFactory());

        // Включаем сортировку для колонок
        surnameColumn.setSortable(true);
        nameColumn.setSortable(true);
        patronymicColumn.setSortable(true);

        // Устанавливаем сортировку по фамилии по умолчанию
        teachersTableView.getSortOrder().add(surnameColumn);

        // Устанавливаем начальные стили
        groupOuterPane.setStyle("-fx-background-color: #FFFFFF;");
        teachersOuterPane.setStyle("-fx-background-color: #FFFFFF;");
        subjectsOuterPane.setStyle("-fx-background-color: #FFFFFF;");

        // Устанавливаем обработчики событий для кнопок
        addButton.setOnMouseClicked(event -> handleAddButtonClick());
        deleteButton.setOnMouseClicked(event -> handleDeleteButtonClick());

        // Устанавливаем обработчики событий для выбора категории
        groupOuterPane.setOnMouseClicked(event -> {
            handlePaneClick(groupOuterPane, "Группа", "groups");
            loadGroupsData();
        });

        teachersOuterPane.setOnMouseClicked(event -> {
            handlePaneClick(teachersOuterPane, "Преподаватель", "teachers");
            loadTeachersData();
        });

        subjectsOuterPane.setOnMouseClicked(event -> {
            handlePaneClick(subjectsOuterPane, "Предмет", "subjects");
            loadSubjectsData();
        });

        // Загрузка данных для преподавателей по умолчанию
        handlePaneClick(teachersOuterPane, "Преподаватель", "teachers");
        loadTeachersData();
    }

    /**
     * Создает фабрику ячеек для простой таблицы с центрированным текстом
     */
    private Callback<TableColumn<SimpleItem, String>, TableCell<SimpleItem, String>> getCenteredSimpleCellFactory() {
        return column -> new TableCell<SimpleItem, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");
                    setFont(Font.font(16));
                }
            }
        };
    }

    /**
     * Создает фабрику ячеек для таблицы преподавателей с центрированным текстом
     */
    private Callback<TableColumn<Teacher, String>, TableCell<Teacher, String>> getCenteredTeacherCellFactory() {
        return column -> new TableCell<Teacher, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");
                    setFont(Font.font(16));
                }
            }
        };
    }

    /**
     * Обрабатывает клик по панели категории
     */
    private void handlePaneClick(Pane clickedPane, String category, String table) {
        // Сбрасываем стили всех Pane на белый фон
        groupOuterPane.setStyle("-fx-background-color: #FFFFFF;");
        teachersOuterPane.setStyle("-fx-background-color: #FFFFFF;");
        subjectsOuterPane.setStyle("-fx-background-color: #FFFFFF;");

        // Устанавливаем зеленый фон для выбранного Pane
        clickedPane.setStyle("-fx-background-color: #8BC34A;");

        // Обновляем текущую категорию и таблицу
        currentCategory = category;
        currentTable = table;

        // Обновляем текст категории
        categoryText.setText(category);

        // Показываем соответствующую таблицу
        if (category.equals("Преподаватель")) {
            simpleTableView.setVisible(false);
            teachersTableView.setVisible(true);
        } else {
            simpleTableView.setVisible(true);
            teachersTableView.setVisible(false);
        }
    }

    /**
     * Обрабатывает клик по кнопке добавления
     */
    private void handleAddButtonClick() {
        switch (currentCategory) {
            case "Группа":
                addGroup();
                break;
            case "Преподаватель":
                addTeacher();
                break;
            case "Предмет":
                addSubject();
                break;
        }
    }

    /**
     * Обрабатывает клик по кнопке удаления
     */
    private void handleDeleteButtonClick() {
        switch (currentCategory) {
            case "Группа":
                deleteGroup();
                break;
            case "Преподаватель":
                deleteTeacher();
                break;
            case "Предмет":
                deleteSubject();
                break;
        }
    }

    /**
     * Добавляет новую группу в базу данных
     */
    private void addGroup() {
        Optional<String> result = DialogUtils.showAddGroupDialog();
        result.ifPresent(groupNumber -> {
            if (!groupNumber.isEmpty()) {
                try {
                    String query = "INSERT INTO groups (group_number) VALUES (?)";
                    Database.executePreparedUpdate(query, groupNumber);
                    DialogUtils.showSuccessDialog("добавлена", "Группа");
                    loadGroupsData();
                } catch (Exception e) {
                    DialogUtils.showErrorDialog("Ошибка при добавлении группы: " + e.getMessage());
                }
            } else {
                DialogUtils.showErrorDialog("Номер группы не может быть пустым!");
            }
        });
    }

    /**
     * Добавляет нового преподавателя в базу данных
     */
    private void addTeacher() {
        Optional<String[]> result = DialogUtils.showAddTeacherDialog();
        result.ifPresent(teacherData -> {
            if (teacherData != null && teacherData.length == 3 &&
                    !teacherData[0].isEmpty() && !teacherData[1].isEmpty()) {
                try {
                    String query = "INSERT INTO teachers (surname, name, patronymic) VALUES (?, ?, ?)";
                    Database.executePreparedUpdate(query,
                            teacherData[0], teacherData[1],
                            teacherData[2].isEmpty() ? null : teacherData[2]);
                    DialogUtils.showSuccessDialog("добавлен", "Преподаватель");
                    loadTeachersData();
                } catch (Exception e) {
                    DialogUtils.showErrorDialog("Ошибка при добавлении преподавателя: " + e.getMessage());
                }
            } else {
                DialogUtils.showErrorDialog("Фамилия и имя не могут быть пустыми!");
            }
        });
    }

    /**
     * Добавляет новый предмет в базу данных
     */
    private void addSubject() {
        Optional<String> result = DialogUtils.showAddSubjectDialog();
        result.ifPresent(subjectName -> {
            if (!subjectName.isEmpty()) {
                try {
                    String query = "INSERT INTO subjects (name) VALUES (?)";
                    Database.executePreparedUpdate(query, subjectName);
                    DialogUtils.showSuccessDialog("добавлен", "Предмет");
                    loadSubjectsData();
                } catch (Exception e) {
                    DialogUtils.showErrorDialog("Ошибка при добавлении предмета: " + e.getMessage());
                }
            } else {
                DialogUtils.showErrorDialog("Название предмета не может быть пустым!");
            }
        });
    }

    /**
     * Удаляет выбранную группу из базы данных
     */
    private void deleteGroup() {
        SimpleItem selectedItem = simpleTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            DialogUtils.showErrorDialog("Выберите группу для удаления!");
            return;
        }

        Optional<ButtonType> result = DialogUtils.showDeleteConfirmationDialog(
                "группу", selectedItem.getValue());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String query = "DELETE FROM groups WHERE group_number = ?";
                Database.executePreparedUpdate(query, selectedItem.getValue());
                DialogUtils.showSuccessDialog("удалена", "Группа");
                loadGroupsData();
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Ошибка при удалении группы: " + e.getMessage());
            }
        }
    }

    /**
     * Удаляет выбранного преподавателя из базы данных
     */
    private void deleteTeacher() {
        Teacher selectedTeacher = teachersTableView.getSelectionModel().getSelectedItem();
        if (selectedTeacher == null) {
            DialogUtils.showErrorDialog("Выберите преподавателя для удаления!");
            return;
        }

        Optional<ButtonType> result = DialogUtils.showDeleteConfirmationDialog(
                "преподавателя", selectedTeacher.getFullName());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String query = "DELETE FROM teachers WHERE surname = ? AND name = ? AND (patronymic = ? OR patronymic IS NULL)";
                Database.executePreparedUpdate(query,
                        selectedTeacher.getSurname(),
                        selectedTeacher.getName(),
                        selectedTeacher.getPatronymic().isEmpty() ? null : selectedTeacher.getPatronymic());
                DialogUtils.showSuccessDialog("удален", "Преподаватель");
                loadTeachersData();
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Ошибка при удалении преподавателя: " + e.getMessage());
            }
        }
    }

    /**
     * Удаляет выбранный предмет из базы данных
     */
    private void deleteSubject() {
        SimpleItem selectedItem = simpleTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            DialogUtils.showErrorDialog("Выберите предмет для удаления!");
            return;
        }

        Optional<ButtonType> result = DialogUtils.showDeleteConfirmationDialog(
                "предмет", selectedItem.getValue());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String query = "DELETE FROM subjects WHERE name = ?";
                Database.executePreparedUpdate(query, selectedItem.getValue());
                DialogUtils.showSuccessDialog("удален", "Предмет");
                loadSubjectsData();
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Ошибка при удалении предмета: " + e.getMessage());
            }
        }
    }

    /**
     * Загружает данные преподавателей из базы данных в таблицу
     */
    private void loadTeachersData() {
        ObservableList<Teacher> data = FXCollections.observableArrayList();
        String query = "SELECT surname, name, patronymic FROM teachers ORDER BY surname, name, patronymic";

        try (ResultSet resultSet = Database.executeQuery(query)) {
            if (resultSet != null) {
                while (resultSet.next()) {
                    Teacher teacher = new Teacher(
                            resultSet.getString("surname"),
                            resultSet.getString("name"),
                            resultSet.getString("patronymic")
                    );
                    data.add(teacher);
                }
            }
            teachersTableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showErrorDialog("Ошибка загрузки данных преподавателей: " + e.getMessage());
        }
    }

    /**
     * Загружает данные групп из базы данных в таблицу
     */
    private void loadGroupsData() {
        ObservableList<SimpleItem> data = FXCollections.observableArrayList();
        String query = "SELECT group_number FROM groups ORDER BY group_number";

        try (ResultSet resultSet = Database.executeQuery(query)) {
            if (resultSet != null) {
                while (resultSet.next()) {
                    data.add(new SimpleItem(resultSet.getString("group_number")));
                }
            }
            simpleTableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showErrorDialog("Ошибка загрузки данных групп: " + e.getMessage());
        }
    }

    /**
     * Загружает данные предметов из базы данных в таблицу
     */
    private void loadSubjectsData() {
        ObservableList<SimpleItem> data = FXCollections.observableArrayList();
        String query = "SELECT name FROM subjects ORDER BY name ASC";

        try (ResultSet resultSet = Database.executeQuery(query)) {
            if (resultSet != null) {
                while (resultSet.next()) {
                    data.add(new SimpleItem(resultSet.getString("name")));
                }
            }
            simpleTableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            DialogUtils.showErrorDialog("Ошибка загрузки данных предметов: " + e.getMessage());
        }
    }

    /**
     * Внутренний класс для представления простых элементов (группы и предметы)
     */
    public static class SimpleItem {
        private final String value;

        public SimpleItem(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Внутренний класс для представления преподавателя
     */
    public static class Teacher {
        private final String surname;
        private final String name;
        private final String patronymic;

        public Teacher(String surname, String name, String patronymic) {
            this.surname = surname != null ? surname : "";
            this.name = name != null ? name : "";
            this.patronymic = patronymic != null ? patronymic : "";
        }

        public String getSurname() {
            return surname;
        }

        public String getName() {
            return name;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public String getFullName() {
            return surname + " " + name + (patronymic.isEmpty() ? "" : " " + patronymic);
        }
    }
}
