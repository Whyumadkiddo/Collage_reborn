package com.example.collage_upgrade.hours_in_week;

import com.example.collage_upgrade.util.PaneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class FirstSemesterController {
    @FXML
    private Pane LocatePane;

    public void initialize(){
        PaneUtils.applyRoundedStyle(LocatePane);
    }
}
