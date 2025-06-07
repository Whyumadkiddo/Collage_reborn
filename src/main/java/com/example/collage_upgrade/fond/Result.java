package com.example.collage_upgrade.fond;

import com.example.collage_upgrade.util.PaneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class Result {
    @FXML
    private Pane LocatePane;
    @FXML
    public void initialize(){
        PaneUtils.applyRoundedStyle(LocatePane);
    }
}
