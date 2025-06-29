package com.example.collage_upgrade.other;

import com.example.collage_upgrade.database.DatabaseInitializer;
import com.example.collage_upgrade.database.HoursRepository;
import com.example.collage_upgrade.model.TableData;
import com.example.collage_upgrade.util.DialogUtils;
import com.example.collage_upgrade.util.PaneUtils;
import javafx.beans.property.SimpleStringProperty;
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

    @FXML private Pane addButton;
    @FXML private Pane editButton;
    @FXML private Pane deleteButton;

    @FXML private TableView<TableData> mainTable;
    @FXML private TableColumn<TableData, String> teacherCol;
    @FXML private TableColumn<TableData, String> subjectCol;
    @FXML private TableColumn<TableData, String> totalCol;

    @FXML private ComboBox<String> groupComboBox;

    private String currentCategory = "Основные";
    private String currentGroup = "";
    private ObservableList<TableData> tableDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DatabaseInitializer.initializeDatabase();
        applyPaneStyles();
        setupPaneHandlers();
        setupButtonHandlers();
        setupTableColumns();
        loadGroups();
        mainTable.setVisible(true);
        loadDataForTable(currentCategory, currentGroup);
    }

    private void loadGroups() {
        try {
            ObservableList<String> groups = HoursRepository.getGroups();
            groupComboBox.setItems(groups);
            groups.add(0, "Все группы");
            groupComboBox.setOnAction(event -> {
                String selectedGroup = groupComboBox.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    currentGroup = selectedGroup.equals("Все группы") ? "" : selectedGroup;
                    loadDataForTable(currentCategory, currentGroup);
                }
            });
            groupComboBox.getSelectionModel().selectFirst();
            currentGroup = "";
        } catch (SQLException e) {
            showErrorDialog("Ошибка при загрузке списка групп: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    private void setupPaneHandlers() {
        mainPane1.setOnMouseClicked(event -> switchCategory("Основные"));
        subGroupPane1.setOnMouseClicked(event -> switchCategory("Подгруппы"));
        subGroupProgrammersPane1.setOnMouseClicked(event -> switchCategory("Подгруппы(программисты)"));
        ppSpecPane1.setOnMouseClicked(event -> switchCategory("ПП Спец"));
        upSpecPane1.setOnMouseClicked(event -> switchCategory("УП Спец"));
        subGroupUpPane1.setOnMouseClicked(event -> switchCategory("Подгруппы(УП)"));
        hoursByPlanPane1.setOnMouseClicked(event -> switchCategory("Часы по учебному плану"));
    }

    private void setupButtonHandlers() {
        addButton.setOnMouseClicked(event -> handleAddButtonClick());
        editButton.setOnMouseClicked(event -> handleEditButtonClick());
        deleteButton.setOnMouseClicked(event -> handleDeleteButtonClick());
    }

    private void switchCategory(String category) {
        resetPaneStyles();
        switch (category) {
            case "Основные": mainPane1.setStyle("-fx-background-color: #8BC34A;"); break;
            case "Подгруппы": subGroupPane1.setStyle("-fx-background-color: #8BC34A;"); break;
            case "Подгруппы(программисты)": subGroupProgrammersPane1.setStyle("-fx-background-color: #8BC34A;"); break;
            case "ПП Спец": ppSpecPane1.setStyle("-fx-background-color: #8BC34A;"); break;
            case "УП Спец": upSpecPane1.setStyle("-fx-background-color: #8BC34A;"); break;
            case "Подгруппы(УП)": subGroupUpPane1.setStyle("-fx-background-color: #8BC34A;"); break;
            case "Часы по учебному плану": hoursByPlanPane1.setStyle("-fx-background-color: #8BC34A;"); break;
        }
        currentCategory = category;
        loadDataForTable(currentCategory, currentGroup);
    }

    private void resetPaneStyles() {
        mainPane1.setStyle("-fx-background-color: #FFFFFF;");
        subGroupPane1.setStyle("-fx-background-color: #FFFFFF;");
        subGroupProgrammersPane1.setStyle("-fx-background-color: #FFFFFF;");
        ppSpecPane1.setStyle("-fx-background-color: #FFFFFF;");
        upSpecPane1.setStyle("-fx-background-color: #FFFFFF;");
        subGroupUpPane1.setStyle("-fx-background-color: #FFFFFF;");
        hoursByPlanPane1.setStyle("-fx-background-color: #FFFFFF;");
    }

    private void setupTableColumns() {
        teacherCol.setCellValueFactory(cellData -> cellData.getValue().teacherProperty());
        subjectCol.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        totalCol.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        // Колонки для 1 семестра
        TableColumn<TableData, String> hoursPerWeek1Col = new TableColumn<>("Часы в неделю");
        TableColumn<TableData, String> hoursByPractice1Col = new TableColumn<>("Часы по практике");
        TableColumn<TableData, String> hoursByPlan1Col = new TableColumn<>("Часы по учебному");

        // Колонки для 2 семестра
        TableColumn<TableData, String> hoursPerWeek2Col = new TableColumn<>("Часы в неделю");
        TableColumn<TableData, String> hoursByPractice2Col = new TableColumn<>("Часы по практике");
        TableColumn<TableData, String> hoursByPlan2Col = new TableColumn<>("Часы по учебному");

        // Настройка колонок для 1 семестра
        hoursPerWeek1Col.setCellValueFactory(cellData -> cellData.getValue().hoursPerWeek1Property());
        hoursByPractice1Col.setCellValueFactory(cellData -> cellData.getValue().hoursByPractice1Property());

        // Настройка колонки "Часы по учебному" для 1 семестра
        hoursByPlan1Col.setCellValueFactory(cellData -> {
            try {
                int weeklyHours = Integer.parseInt(cellData.getValue().getHoursPerWeek1());
                int practiceHours = Integer.parseInt(cellData.getValue().getHoursByPractice1());
                return new SimpleStringProperty(String.valueOf(weeklyHours + practiceHours));
            } catch (NumberFormatException e) {
                return new SimpleStringProperty("0");
            }
        });

        // Настройка колонок для 2 семестра
        hoursPerWeek2Col.setCellValueFactory(cellData -> cellData.getValue().hoursPerWeek2Property());
        hoursByPractice2Col.setCellValueFactory(cellData -> cellData.getValue().hoursByPractice2Property());

        // Настройка колонки "Часы по учебному" для 2 семестра
        hoursByPlan2Col.setCellValueFactory(cellData -> {
            try {
                int weeklyHours = Integer.parseInt(cellData.getValue().getHoursPerWeek2());
                int practiceHours = Integer.parseInt(cellData.getValue().getHoursByPractice2());
                return new SimpleStringProperty(String.valueOf(weeklyHours + practiceHours));
            } catch (NumberFormatException e) {
                return new SimpleStringProperty("0");
            }
        });

        // Группировка колонок по семестрам
        TableColumn<TableData, ?> semester1Group = new TableColumn<>("1 Семестр");
        semester1Group.getColumns().addAll(hoursPerWeek1Col, hoursByPractice1Col, hoursByPlan1Col);

        TableColumn<TableData, ?> semester2Group = new TableColumn<>("2 Семестр");
        semester2Group.getColumns().addAll(hoursPerWeek2Col, hoursByPractice2Col, hoursByPlan2Col);

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


    private void loadDataForTable(String category, String groupNumber) {
        try {
            tableDataList.clear();
            ObservableList<TableData> data = HoursRepository.loadTableData(category, groupNumber);
            tableDataList.addAll(data);
            mainTable.setItems(tableDataList);
            mainTable.refresh();
            System.out.println("Загружено " + data.size() + " записей для категории: " + category +
                    (groupNumber.isEmpty() ? "" : " и группы: " + groupNumber));
        } catch (SQLException e) {
            showErrorDialog("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAddButtonClick() {
        try {
            ObservableList<String> teachers = HoursRepository.getTeachers();
            ObservableList<String> subjects = HoursRepository.getSubjects();

            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Добавить запись");
            dialog.setHeaderText("Введите данные для новой записи");

            ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            ComboBox<String> teacherComboBox = new ComboBox<>(teachers);
            teacherComboBox.setPromptText("Выберите преподавателя");
            ComboBox<String> subjectComboBox = new ComboBox<>(subjects);
            subjectComboBox.setPromptText("Выберите предмет");

            // Поля для первого семестра
            TextField hoursPerWeek1Field = new TextField();
            hoursPerWeek1Field.setPromptText("Часы в неделю (1 семестр)");
            TextField hoursByPractice1Field = new TextField();
            hoursByPractice1Field.setPromptText("Часы по практике (1 семестр)");

            // Поля для второго семестра
            TextField hoursPerWeek2Field = new TextField();
            hoursPerWeek2Field.setPromptText("Часы в неделю (2 семестр)");
            TextField hoursByPractice2Field = new TextField();
            hoursByPractice2Field.setPromptText("Часы по практике (2 семестр)");

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
            grid.add(new Label("1 Семестр - Часы по практике:"), 0, 3);
            grid.add(hoursByPractice1Field, 1, 3);
            grid.add(new Label("2 Семестр - Часы в неделю:"), 0, 4);
            grid.add(hoursPerWeek2Field, 1, 4);
            grid.add(new Label("2 Семестр - Часы по практике:"), 0, 5);
            grid.add(hoursByPractice2Field, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    try {
                        // Рассчитываем часы по учебному для каждого семестра
                        int plan1 = Integer.parseInt(hoursPerWeek1Field.getText()) +
                                Integer.parseInt(hoursByPractice1Field.getText());
                        int plan2 = Integer.parseInt(hoursPerWeek2Field.getText()) +
                                Integer.parseInt(hoursByPractice2Field.getText());

                        // Итоговое количество часов - сумма часов по учебному за оба семестра
                        int total = plan1 + plan2;

                        TableData newData = new TableData(
                                teacherComboBox.getValue(),
                                subjectComboBox.getValue(),
                                "1",
                                hoursPerWeek1Field.getText(),
                                String.valueOf(plan1), // Часы по учебному для 1 семестра
                                hoursByPractice1Field.getText(),
                                "2",
                                hoursPerWeek2Field.getText(),
                                String.valueOf(plan2), // Часы по учебному для 2 семестра
                                hoursByPractice2Field.getText(),
                                String.valueOf(total)
                        );

                        try {
                            HoursRepository.saveTableData(newData, currentCategory, currentGroup);
                            tableDataList.add(newData);
                            mainTable.setItems(tableDataList);
                            mainTable.refresh();
                            return new Pair<>(teacherComboBox.getValue(), subjectComboBox.getValue());
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


    private void handleEditButtonClick() {
        TableData selectedItem = mainTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showErrorDialog("Выберите запись для редактирования!");
            return;
        }
        try {
            ObservableList<String> teachers = HoursRepository.getTeachers();
            ObservableList<String> subjects = HoursRepository.getSubjects();

            Dialog<TableData> dialog = new Dialog<>();
            dialog.setTitle("Редактировать запись");
            dialog.setHeaderText("Редактирование записи");

            ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            ComboBox<String> teacherComboBox = new ComboBox<>(teachers);
            teacherComboBox.setValue(selectedItem.getTeacher());
            teacherComboBox.setPromptText("Выберите преподавателя");
            ComboBox<String> subjectComboBox = new ComboBox<>(subjects);
            subjectComboBox.setValue(selectedItem.getSubject());
            subjectComboBox.setPromptText("Выберите предмет");

            TextField hoursPerWeek1Field = new TextField(selectedItem.getHoursPerWeek1());
            hoursPerWeek1Field.setPromptText("Часы в неделю (1 семестр)");
            TextField hoursByPractice1Field = new TextField(selectedItem.getHoursByPractice1());
            hoursByPractice1Field.setPromptText("Часы по практике (1 семестр)");

            TextField hoursPerWeek2Field = new TextField(selectedItem.getHoursPerWeek2());
            hoursPerWeek2Field.setPromptText("Часы в неделю (2 семестр)");
            TextField hoursByPractice2Field = new TextField(selectedItem.getHoursByPractice2());
            hoursByPractice2Field.setPromptText("Часы по практике (2 семестр)");

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
            grid.add(new Label("1 Семестр - Часы по практике:"), 0, 3);
            grid.add(hoursByPractice1Field, 1, 3);
            grid.add(new Label("2 Семестр - Часы в неделю:"), 0, 4);
            grid.add(hoursPerWeek2Field, 1, 4);
            grid.add(new Label("2 Семестр - Часы по практике:"), 0, 5);
            grid.add(hoursByPractice2Field, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    try {
                        // Рассчитываем часы по учебному для каждого семестра
                        int plan1 = Integer.parseInt(hoursPerWeek1Field.getText()) +
                                Integer.parseInt(hoursByPractice1Field.getText());
                        int plan2 = Integer.parseInt(hoursPerWeek2Field.getText()) +
                                Integer.parseInt(hoursByPractice2Field.getText());

                        // Итоговое количество часов - сумма часов по учебному за оба семестра
                        int total = plan1 + plan2;

                        selectedItem.setTeacher(teacherComboBox.getValue());
                        selectedItem.setSubject(subjectComboBox.getValue());
                        selectedItem.setHoursPerWeek1(hoursPerWeek1Field.getText());
                        selectedItem.setHoursByPlan1(String.valueOf(plan1));
                        selectedItem.setHoursByPractice1(hoursByPractice1Field.getText());
                        selectedItem.setHoursPerWeek2(hoursPerWeek2Field.getText());
                        selectedItem.setHoursByPlan2(String.valueOf(plan2));
                        selectedItem.setHoursByPractice2(hoursByPractice2Field.getText());
                        selectedItem.setTotal(String.valueOf(total));

                        try {
                            HoursRepository.updateTableData(selectedItem, currentCategory, currentGroup);
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
                HoursRepository.deleteTableData(selectedItem.getTeacher(), currentCategory, currentGroup);
                tableDataList.remove(selectedItem);
                mainTable.setItems(tableDataList);
                mainTable.refresh();
            } catch (SQLException e) {
                showErrorDialog("Ошибка при удалении данных: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
