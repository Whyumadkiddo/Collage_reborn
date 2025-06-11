package com.example.collage_upgrade.util;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public class DialogUtils {

    public static Optional<String> showAddGroupDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Добавить группу");
        dialog.setHeaderText("Введите данные для новой группы");

        ButtonType addButton = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField groupNumber = new TextField();
        groupNumber.setPromptText("Номер группы");

        grid.add(new Label("Номер группы:"), 0, 0);
        grid.add(groupNumber, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return groupNumber.getText().trim();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<String> showAddSubjectDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Добавить предмет");
        dialog.setHeaderText("Введите данные для нового предмета");

        ButtonType addButton = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField subjectName = new TextField();
        subjectName.setPromptText("Название предмета");

        grid.add(new Label("Название предмета:"), 0, 0);
        grid.add(subjectName, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return subjectName.getText().trim();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<String[]> showAddTeacherDialog() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Добавить преподавателя");
        dialog.setHeaderText("Введите данные для нового преподавателя");

        ButtonType addButton = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField surname = new TextField();
        TextField name = new TextField();
        TextField patronymic = new TextField();

        surname.setPromptText("Фамилия");
        name.setPromptText("Имя");
        patronymic.setPromptText("Отчество");

        grid.add(new Label("Фамилия:"), 0, 0);
        grid.add(surname, 1, 0);
        grid.add(new Label("Имя:"), 0, 1);
        grid.add(name, 1, 1);
        grid.add(new Label("Отчество:"), 0, 2);
        grid.add(patronymic, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new String[]{
                        surname.getText().trim(),
                        name.getText().trim(),
                        patronymic.getText().trim()
                };
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<ButtonType> showDeleteConfirmationDialog(String itemType, String itemName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удалить " + itemType + "?");
        alert.setContentText("Вы действительно хотите удалить " + itemType + ": " + itemName + "?");

        return alert.showAndWait();
    }

    public static void showSuccessDialog(String operation, String itemType) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText(itemType + " успешно " + operation + "!");

        alert.showAndWait();
    }

    public static void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
