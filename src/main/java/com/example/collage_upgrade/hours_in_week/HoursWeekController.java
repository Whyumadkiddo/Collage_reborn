package com.example.collage_upgrade.hours_in_week;

import com.example.collage_upgrade.util.PaneUtils;
import com.example.collage_upgrade.util.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class HoursWeekController {

    @FXML
    private Pane firstSemPane;

    @FXML
    private Pane secondSemPane;

    @FXML
    public void initialize() {
        PaneUtils.applyRoundedStyle(firstSemPane);
        PaneUtils.applyRoundedStyle(secondSemPane);

        firstSemPane.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/hours_in_week/first_semester.fxml", "Первый Семестр", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        secondSemPane.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/hours_in_week/second_semester.fxml", "Второй Семестр", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
