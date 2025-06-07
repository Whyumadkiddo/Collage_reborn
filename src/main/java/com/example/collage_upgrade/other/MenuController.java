package com.example.collage_upgrade.other;

import com.example.collage_upgrade.util.PaneUtils;
import com.example.collage_upgrade.util.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MenuController {

    @FXML
    private Pane myPane1;

    @FXML
    private Pane myPane2;

    @FXML
    private Pane myPane3;

    @FXML
    private Pane myPane4;

    @FXML
    public void initialize() {
        PaneUtils.applyRoundedStyle(myPane1);
        PaneUtils.applyRoundedStyle(myPane2);
        PaneUtils.applyRoundedStyle(myPane3);
        PaneUtils.applyRoundedStyle(myPane4);

        myPane1.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/other/hours_groups.fxml", "Часы по группам", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        myPane2.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/fondFxml/hour_fond_main.fxml", "Фонд Часов", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myPane3.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/hours_in_week/hours_week.fxml", "Часы в неделю", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myPane4.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/other/elements.fxml", "Элементы", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
