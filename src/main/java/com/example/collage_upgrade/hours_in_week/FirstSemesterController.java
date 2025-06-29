package com.example.collage_upgrade.hours_in_week;

import com.example.collage_upgrade.database.HoursRepository;
import com.example.collage_upgrade.model.TableData;
import com.example.collage_upgrade.util.PaneUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FirstSemesterController {

    @FXML private Pane LocatePane;
    @FXML private TableView<GroupHoursSummary> summaryTable;
    @FXML private TableColumn<GroupHoursSummary, String> groupColumn;
    @FXML private TableColumn<GroupHoursSummary, String> theoryColumn;
    @FXML private TableColumn<GroupHoursSummary, String> practiceColumn;
    @FXML private TableColumn<GroupHoursSummary, String> totalColumn;

    public void initialize() {
        PaneUtils.applyRoundedStyle(LocatePane);
        setupTableColumns();
        loadSummaryData();
    }

    private void setupTableColumns() {
        // Устанавливаем стиль шрифта для колонок
        Font columnFont = Font.font("System", 16);

        // Настройка колонок с увеличенным шрифтом
        if (groupColumn != null) {
            groupColumn.setCellValueFactory(cellData -> cellData.getValue().groupProperty());
            groupColumn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            groupColumn.setPrefWidth(250);
        }

        if (theoryColumn != null) {
            theoryColumn.setCellValueFactory(cellData -> cellData.getValue().theoryProperty());
            theoryColumn.setStyle("-fx-font-size: 16px;");
            theoryColumn.setPrefWidth(220);
        }

        if (practiceColumn != null) {
            practiceColumn.setCellValueFactory(cellData -> cellData.getValue().practiceProperty());
            practiceColumn.setStyle("-fx-font-size: 16px;");
            practiceColumn.setPrefWidth(220);
        }

        if (totalColumn != null) {
            totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
            totalColumn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            totalColumn.setPrefWidth(200);
        }
    }

    private void loadSummaryData() {
        try {
            ObservableList<GroupHoursSummary> summaryData = FXCollections.observableArrayList();
            ObservableList<String> groups = HoursRepository.getGroups();

            Map<String, GroupHoursSummary> groupSummaryMap = new HashMap<>();

            for (String group : groups) {
                groupSummaryMap.put(group, new GroupHoursSummary(group, "0", "0", "0"));
            }

            String[] categories = {"Основные", "Подгруппы", "Подгруппы(программисты)",
                    "ПП Спец", "УП Спец", "Подгруппы(УП)", "Часы по учебному плану"};

            for (String category : categories) {
                ObservableList<TableData> data = HoursRepository.loadTableData(category, "");
                for (TableData item : data) {
                    String group = item.getSemester1();
                    if (group != null && groupSummaryMap.containsKey(group)) {
                        GroupHoursSummary summary = groupSummaryMap.get(group);
                        try {
                            int currentTheory = Integer.parseInt(summary.getTheory());
                            int theoryHours = Integer.parseInt(item.getHoursPerWeek1()) +
                                    Integer.parseInt(item.getHoursByPractice1());
                            summary.setTheory(String.valueOf(currentTheory + theoryHours));

                            int currentPractice = Integer.parseInt(summary.getPractice());
                            int practiceHours = Integer.parseInt(item.getHoursByPractice1());
                            summary.setPractice(String.valueOf(currentPractice + practiceHours));

                            int total = currentTheory + theoryHours + currentPractice + practiceHours;
                            summary.setTotal(String.valueOf(total));
                        } catch (NumberFormatException e) {
                            System.err.println("Ошибка преобразования чисел для группы " + group);
                        }
                    }
                }
            }

            summaryData.addAll(groupSummaryMap.values());
            if (summaryTable != null) {
                summaryTable.setItems(summaryData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class GroupHoursSummary {
        private final SimpleStringProperty group;
        private final SimpleStringProperty theory;
        private final SimpleStringProperty practice;
        private final SimpleStringProperty total;

        public GroupHoursSummary(String group, String theory, String practice, String total) {
            this.group = new SimpleStringProperty(group);
            this.theory = new SimpleStringProperty(theory);
            this.practice = new SimpleStringProperty(practice);
            this.total = new SimpleStringProperty(total);
        }

        public String getGroup() { return group.get(); }
        public String getTheory() { return theory.get(); }
        public String getPractice() { return practice.get(); }
        public String getTotal() { return total.get(); }

        public void setTheory(String theory) { this.theory.set(theory); }
        public void setPractice(String practice) { this.practice.set(practice); }
        public void setTotal(String total) { this.total.set(total); }

        public SimpleStringProperty groupProperty() { return group; }
        public SimpleStringProperty theoryProperty() { return theory; }
        public SimpleStringProperty practiceProperty() { return practice; }
        public SimpleStringProperty totalProperty() { return total; }
    }
}
