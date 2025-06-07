package com.example.collage_upgrade.fond;

import com.example.collage_upgrade.util.PaneUtils;
import com.example.collage_upgrade.util.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class HourFondMainController {
    @FXML
    private Pane frstSem;

    @FXML
    private Pane scndSem;

    @FXML
    private Pane frstGroup;

    @FXML
    private Pane scndGroup;

    @FXML
    private Pane result;

    @FXML
    public void initialize(){
        PaneUtils.applyRoundedStyle(frstSem);
        PaneUtils.applyRoundedStyle(scndSem);
        PaneUtils.applyRoundedStyle(frstGroup);
        PaneUtils.applyRoundedStyle(scndGroup);
        PaneUtils.applyRoundedStyle(result);
        frstSem.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/fondFxml/group_first_sem.fxml", "Первый Семестр", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        scndSem.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/fondFxml/group_second_sem.fxml", "Второй семестр", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        frstGroup.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/fondFxml/underGroup_first.fxml", "Первые подгруппы", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        scndGroup.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/fondFxml/underGroup_second.fxml", "Вторые подгруппы", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        result.setOnMouseClicked(event -> {
            try {
                SceneUtils.openNewWindow("/com/example/collage_upgrade/fondFxml/result.fxml", "Результат", 1000, 585);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
