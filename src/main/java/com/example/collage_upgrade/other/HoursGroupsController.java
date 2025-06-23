package com.example.collage_upgrade.other;

import com.example.collage_upgrade.database.DatabaseInitializer;
import com.example.collage_upgrade.database.HoursRepository;
import com.example.collage_upgrade.model.TableData;
import com.example.collage_upgrade.util.DialogUtils;
import com.example.collage_upgrade.util.PaneUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HoursGroupsController {
    @FXML private Pane mainPane1;
    @FXML private Pane mainInnerPane1;
    @FXML private Pane subGroupPane1;
    @FXML private Pane subGroupInnerPane1;
    @FXML private Pane subGroupProgrammersPane1;
    @FXML private Pane subGroupProgrammersInnerPane1;
    @FXML private Pane ppSpecPane1;
    @FXML private Pane ppSpecInnerPane1;
    @FXML private Pane upSpecPane1;
    @FXML private Pane upSpecInnerPane1;
    @FXML private Pane subGroupUpPane1;
    @FXML private Pane subGroupUpInnerPane1;
    @FXML private Pane hoursByPlanPane1;
    @FXML private Pane hoursByPlanInnerPane1;
    @FXML private Pane locatePane;
    @FXML private Pane locateGroupPane;

    // Кнопки управления
    @FXML private Pane addButton;
    @FXML private Pane editButton;
    @FXML private Pane deleteButton;

    // Таблица
    @FXML private TableView<TableData> mainTable;

    // Колонки таблицы
    @FXML private TableColumn<TableData, String> teacherCol;
    @FXML private TableColumn<TableData, String> subjectCol;
    @FXML private TableColumn<TableData, String> totalCol;

    // Поле для выбора группы
    @FXML private ComboBox<String> groupComboBox;

    private String currentCategory = "Основные";
    private String currentGroup = "";
    private ObservableList<TableData> tableDataList = FXCollections.observableArrayList();

    /**
     * Инициализация контроллера, настройка таблицы и загрузка данных
     */
    @FXML
    public void initialize() {
        // Инициализируем базу данных
        DatabaseInitializer.initializeDatabase();

        // Применяем стили ко всем панелям
        applyPaneStyles();

        // Настройка обработчиков событий
        setupPaneHandlers();
        setupButtonHandlers();

        // Инициализация таблицы
        setupTableColumns();

        // Загружаем список групп
        loadGroups();

        // По умолчанию показываем основную таблицу
        mainTable.setVisible(true);

        // Загружаем данные для основной категории
        loadDataForTable(currentCategory, currentGroup);
    }

    /**
     * Загружает список групп в ComboBox
     */
    private void loadGroups() {
        try {
            ObservableList<String> groups = HoursRepository.getGroups();
            groupComboBox.setItems(groups);

            // Добавляем пустой элемент для возможности выбора без группы
            groups.add(0, "Все группы");

            // Устанавливаем обработчик выбора группы
            groupComboBox.setOnAction(event -> {
                String selectedGroup = groupComboBox.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    currentGroup = selectedGroup.equals("Все группы") ? "" : selectedGroup;
                    loadDataForTable(currentCategory, currentGroup);
                }
            });

            // Выбираем первый элемент по умолчанию
            groupComboBox.getSelectionModel().selectFirst();
            currentGroup = "";
        } catch (SQLException e) {
            showErrorDialog("Ошибка при загрузке списка групп: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Применяет стили ко всем панелям
     */
    private void applyPaneStyles() {
        PaneUtils.applyRoundedStyle(mainPane1);
        PaneUtils.applyRoundedStyle(mainInnerPane1);
        PaneUtils.applyRoundedStyle(subGroupPane1);
        PaneUtils.applyRoundedStyle(subGroupInnerPane1);
        PaneUtils.applyRoundedStyle(subGroupProgrammersPane1);
        PaneUtils.applyRoundedStyle(subGroupProgrammersInnerPane1);
        PaneUtils.applyRoundedStyle(ppSpecPane1);
        PaneUtils.applyRoundedStyle(ppSpecInnerPane1);
        PaneUtils.applyRoundedStyle(upSpecPane1);
        PaneUtils.applyRoundedStyle(upSpecInnerPane1);
        PaneUtils.applyRoundedStyle(subGroupUpPane1);
        PaneUtils.applyRoundedStyle(subGroupUpInnerPane1);
        PaneUtils.applyRoundedStyle(hoursByPlanPane1);
        PaneUtils.applyRoundedStyle(hoursByPlanInnerPane1);
        PaneUtils.applyRoundedStyle(locatePane);
        PaneUtils.applyRoundedStyle(locateGroupPane);
        PaneUtils.applyRoundedStyle(addButton);
        PaneUtils.applyRoundedStyle(editButton);
        PaneUtils.applyRoundedStyle(deleteButton);
    }

    /**
     * Настраивает обработчики событий для панелей категорий
     */
    private void setupPaneHandlers() {
        mainPane1.setOnMouseClicked(event -> {
            switchCategory("Основные");
        });

        subGroupPane1.setOnMouseClicked(event -> {
            switchCategory("Подгруппы");
        });

        subGroupProgrammersPane1.setOnMouseClicked(event -> {
            switchCategory("Подгруппы(программисты)");
        });

        ppSpecPane1.setOnMouseClicked(event -> {
            switchCategory("ПП Спец");
        });

        upSpecPane1.setOnMouseClicked(event -> {
            switchCategory("УП Спец");
        });

        subGroupUpPane1.setOnMouseClicked(event -> {
            switchCategory("Подгруппы(УП)");
        });

        hoursByPlanPane1.setOnMouseClicked(event -> {
            switchCategory("Часы по учебному плану");
        });
    }

    /**
     * Настраивает обработчики событий для кнопок управления
     */
    private void setupButtonHandlers() {
        addButton.setOnMouseClicked(event -> handleAddButtonClick());
        editButton.setOnMouseClicked(event -> handleEditButtonClick());
        deleteButton.setOnMouseClicked(event -> handleDeleteButtonClick());
    }

    /**
     * Переключает текущую категорию и обновляет таблицу
     */
    private void switchCategory(String category) {
        // Сбрасываем стили всех панелей
        resetPaneStyles();

        // Подсвечиваем выбранную панель в зависимости от категории
        switch (category) {
            case "Основные":
                mainPane1.setStyle("-fx-background-color: #8BC34A;");
                break;
            case "Подгруппы":
                subGroupPane1.setStyle("-fx-background-color: #8BC34A;");
                break;
            case "Подгруппы(программисты)":
                subGroupProgrammersPane1.setStyle("-fx-background-color: #8BC34A;");
                break;
            case "ПП Спец":
                ppSpecPane1.setStyle("-fx-background-color: #8BC34A;");
                break;
            case "УП Спец":
                upSpecPane1.setStyle("-fx-background-color: #8BC34A;");
                break;
            case "Подгруппы(УП)":
                subGroupUpPane1.setStyle("-fx-background-color: #8BC34A;");
                break;
            case "Часы по учебному плану":
                hoursByPlanPane1.setStyle("-fx-background-color: #8BC34A;");
                break;
        }

        currentCategory = category;

        // Загружаем данные для выбранной категории и группы
        loadDataForTable(currentCategory, currentGroup);
    }

    /**
     * Сбрасывает стили всех панелей категорий
     */
    private void resetPaneStyles() {
        mainPane1.setStyle("-fx-background-color: #FFFFFF;");
        subGroupPane1.setStyle("-fx-background-color: #FFFFFF;");
        subGroupProgrammersPane1.setStyle("-fx-background-color: #FFFFFF;");
        ppSpecPane1.setStyle("-fx-background-color: #FFFFFF;");
        upSpecPane1.setStyle("-fx-background-color: #FFFFFF;");
        subGroupUpPane1.setStyle("-fx-background-color: #FFFFFF;");
        hoursByPlanPane1.setStyle("-fx-background-color: #FFFFFF;");
    }

    /**
     * Настраивает колонки таблицы
     */
    private void setupTableColumns() {
        // Настройка основных колонок
        teacherCol.setCellValueFactory(cellData -> cellData.getValue().teacherProperty());
        subjectCol.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        totalCol.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        // Создаем колонки для семестров
        TableColumn<TableData, String> hoursPerWeek1Col = new TableColumn<>("Часы в неделю");
        TableColumn<TableData, String> hoursByPlan1Col = new TableColumn<>("Часы по учебному");
        TableColumn<TableData, String> hoursPerWeek2Col = new TableColumn<>("Часы в неделю");
        TableColumn<TableData, String> hoursByPlan2Col = new TableColumn<>("Часы по учебному");

        // Настраиваем колонки для семестров
        hoursPerWeek1Col.setCellValueFactory(cellData -> cellData.getValue().hoursPerWeek1Property());
        hoursByPlan1Col.setCellValueFactory(cellData -> cellData.getValue().hoursByPlan1Property());
        hoursPerWeek2Col.setCellValueFactory(cellData -> cellData.getValue().hoursPerWeek2Property());
        hoursByPlan2Col.setCellValueFactory(cellData -> cellData.getValue().hoursByPlan2Property());

        // Создаем колонку для 1 семестра
        TableColumn<TableData, ?> semester1Group = new TableColumn<>("1 Семестр");
        semester1Group.getColumns().addAll(hoursPerWeek1Col, hoursByPlan1Col);

        // Создаем колонку для 2 семестра
        TableColumn<TableData, ?> semester2Group = new TableColumn<>("2 Семестр");
        semester2Group.getColumns().addAll(hoursPerWeek2Col, hoursByPlan2Col);

        // Создаем список колонок
        List<TableColumn<TableData, ?>> columns = new ArrayList<>();
        columns.add(teacherCol);
        columns.add(semester1Group);
        columns.add(semester2Group);
        columns.add(subjectCol);
        columns.add(totalCol);

        // Применяем колонки к таблице
        mainTable.getColumns().setAll(columns);
    }

    /**
     * Загружает данные для таблицы из базы данных
     */
    private void loadDataForTable(String category, String groupNumber) {
        try {
            // Очищаем текущие данные
            tableDataList.clear();

            // Загружаем данные из базы
            ObservableList<TableData> data = HoursRepository.loadTableData(category, groupNumber);

            // Добавляем данные в список
            tableDataList.addAll(data);

            // Устанавливаем данные в таблицу
            mainTable.setItems(tableDataList);

            // Обновляем таблицу
            mainTable.refresh();

            // Выводим данные в консоль для диагностики
            System.out.println("Загружено " + data.size() + " записей для категории: " + category +
                    (groupNumber.isEmpty() ? "" : " и группы: " + groupNumber));
        } catch (SQLException e) {
            showErrorDialog("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает клик по кнопке добавления записи
     */
    private void handleAddButtonClick() {
        try {
            // Получаем данные из базы
            ObservableList<String> teachers = HoursRepository.getTeachers();
            ObservableList<String> subjects = HoursRepository.getSubjects();

            // Создаем диалоговое окно для добавления записи
            Dialog<TableData> dialog = new Dialog<>();
            dialog.setTitle("Добавить запись");
            dialog.setHeaderText("Введите данные для новой записи");

            // Настраиваем кнопки
            ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            // Создаем элементы управления
            ComboBox<String> teacherComboBox = new ComboBox<>(teachers);
            teacherComboBox.setPromptText("Выберите преподавателя");

            ComboBox<String> subjectComboBox = new ComboBox<>(subjects);
            subjectComboBox.setPromptText("Выберите предмет");

            // Поля для первого семестра
            TextField hoursPerWeek1Field = new TextField();
            hoursPerWeek1Field.setPromptText("Часы в неделю");

            TextField hoursByPlan1Field = new TextField();
            hoursByPlan1Field.setPromptText("Часы по учебному");

            // Поля для второго семестра
            TextField hoursPerWeek2Field = new TextField();
            hoursPerWeek2Field.setPromptText("Часы в неделю");

            TextField hoursByPlan2Field = new TextField();
            hoursByPlan2Field.setPromptText("Часы по учебному");

            // Создаем сетку для элементов
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            grid.add(new Label("Преподаватель:"), 0, 0);
            grid.add(teacherComboBox, 1, 0);
            grid.add(new Label("Предмет:"), 0, 1);
            grid.add(subjectComboBox, 1, 1);
            grid.add(new Label("1 Семестр - Часы в неделю:"), 0, 2);
            grid.add(hoursPerWeek1Field, 1, 2);
            grid.add(new Label("1 Семестр - Часы по учебному:"), 0, 3);
            grid.add(hoursByPlan1Field, 1, 3);
            grid.add(new Label("2 Семестр - Часы в неделю:"), 0, 4);
            grid.add(hoursPerWeek2Field, 1, 4);
            grid.add(new Label("2 Семестр - Часы по учебному:"), 0, 5);
            grid.add(hoursByPlan2Field, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Преобразуем результат в объект TableData
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    try {
                        int total = Integer.parseInt(hoursByPlan1Field.getText()) +
                                Integer.parseInt(hoursByPlan2Field.getText());

                        TableData newData = new TableData(
                                teacherComboBox.getValue(),
                                subjectComboBox.getValue(),
                                "1",
                                hoursPerWeek1Field.getText(),
                                hoursByPlan1Field.getText(),
                                "2",
                                hoursPerWeek2Field.getText(),
                                hoursByPlan2Field.getText(),
                                String.valueOf(total)
                        );

                        try {
                            // Сохраняем данные в базу
                            HoursRepository.saveTableData(newData, currentCategory, currentGroup);

                            // Добавляем данные в список и обновляем таблицу
                            tableDataList.add(newData);
                            mainTable.setItems(tableDataList);
                            mainTable.refresh();

                            return newData;
                        } catch (SQLException e) {
                            showErrorDialog("Ошибка при сохранении данных: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        showErrorDialog("Некорректное значение часов. Введите числовое значение.");
                        return null;
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } catch (SQLException e) {
            showErrorDialog("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает клик по кнопке редактирования записи
     */
    private void handleEditButtonClick() {
        TableData selectedItem = mainTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showErrorDialog("Выберите запись для редактирования!");
            return;
        }

        try {
            // Получаем данные из базы
            ObservableList<String> teachers = HoursRepository.getTeachers();
            ObservableList<String> subjects = HoursRepository.getSubjects();

            // Создаем диалоговое окно для редактирования записи
            Dialog<TableData> dialog = new Dialog<>();
            dialog.setTitle("Редактировать запись");
            dialog.setHeaderText("Редактирование записи");

            // Настраиваем кнопки
            ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Создаем элементы управления
            ComboBox<String> teacherComboBox = new ComboBox<>(teachers);
            teacherComboBox.setValue(selectedItem.getTeacher());
            teacherComboBox.setPromptText("Выберите преподавателя");

            ComboBox<String> subjectComboBox = new ComboBox<>(subjects);
            subjectComboBox.setValue(selectedItem.getSubject());
            subjectComboBox.setPromptText("Выберите предмет");

            // Поля для первого семестра
            TextField hoursPerWeek1Field = new TextField(selectedItem.getHoursPerWeek1());
            hoursPerWeek1Field.setPromptText("Часы в неделю");

            TextField hoursByPlan1Field = new TextField(selectedItem.getHoursByPlan1());
            hoursByPlan1Field.setPromptText("Часы по учебному");

            // Поля для второго семестра
            TextField hoursPerWeek2Field = new TextField(selectedItem.getHoursPerWeek2());
            hoursPerWeek2Field.setPromptText("Часы в неделю");

            TextField hoursByPlan2Field = new TextField(selectedItem.getHoursByPlan2());
            hoursByPlan2Field.setPromptText("Часы по учебному");

            // Создаем сетку для элементов
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            grid.add(new Label("Преподаватель:"), 0, 0);
            grid.add(teacherComboBox, 1, 0);
            grid.add(new Label("Предмет:"), 0, 1);
            grid.add(subjectComboBox, 1, 1);
            grid.add(new Label("1 Семестр - Часы в неделю:"), 0, 2);
            grid.add(hoursPerWeek1Field, 1, 2);
            grid.add(new Label("1 Семестр - Часы по учебному:"), 0, 3);
            grid.add(hoursByPlan1Field, 1, 3);
            grid.add(new Label("2 Семестр - Часы в неделю:"), 0, 4);
            grid.add(hoursPerWeek2Field, 1, 4);
            grid.add(new Label("2 Семестр - Часы по учебному:"), 0, 5);
            grid.add(hoursByPlan2Field, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Преобразуем результат в объект TableData
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    try {
                        int total = Integer.parseInt(hoursByPlan1Field.getText()) +
                                Integer.parseInt(hoursByPlan2Field.getText());

                        // Обновляем данные в объекте
                        selectedItem.setTeacher(teacherComboBox.getValue());
                        selectedItem.setSubject(subjectComboBox.getValue());
                        selectedItem.setHoursPerWeek1(hoursPerWeek1Field.getText());
                        selectedItem.setHoursByPlan1(hoursByPlan1Field.getText());
                        selectedItem.setHoursPerWeek2(hoursPerWeek2Field.getText());
                        selectedItem.setHoursByPlan2(hoursByPlan2Field.getText());
                        selectedItem.setTotal(String.valueOf(total));

                        try {
                            // Обновляем данные в базе
                            HoursRepository.updateTableData(selectedItem, currentCategory, currentGroup);

                            // Обновляем таблицу
                            mainTable.refresh();

                            return selectedItem;
                        } catch (SQLException e) {
                            showErrorDialog("Ошибка при обновлении данных: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        showErrorDialog("Некорректное значение часов. Введите числовое значение.");
                        return null;
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } catch (SQLException e) {
            showErrorDialog("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает клик по кнопке удаления записи
     */
    private void handleDeleteButtonClick() {
        TableData selectedItem = mainTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showErrorDialog("Выберите запись для удаления!");
            return;
        }

        Optional<ButtonType> result = DialogUtils.showDeleteConfirmationDialog(
                "запись", selectedItem.getTeacher() + " - " + selectedItem.getSubject());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Удаляем данные из базы
                HoursRepository.deleteTableData(selectedItem.getTeacher(), currentCategory, currentGroup);

                // Удаляем данные из списка
                tableDataList.remove(selectedItem);

                // Обновляем таблицу
                mainTable.setItems(tableDataList);
                mainTable.refresh();
            } catch (SQLException e) {
                showErrorDialog("Ошибка при удалении данных: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Показывает диалоговое окно с ошибкой
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
