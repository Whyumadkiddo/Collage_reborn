package com.example.collage_upgrade.other;

import com.example.collage_upgrade.util.PaneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class ElementsController {

    @FXML
    private Pane groupOuterPane;

    @FXML
    private Pane groupInnerPane;

    @FXML
    private Pane teachersOuterPane;

    @FXML
    private Pane teachersInnerPane;

    @FXML
    private Pane subjectsOuterPane;

    @FXML
    private Pane subjectsInnerPane;

    @FXML
    public void initialize() {
        PaneUtils.applyRoundedStyle(groupOuterPane);
        PaneUtils.applyRoundedStyle(groupInnerPane);
        PaneUtils.applyRoundedStyle(teachersOuterPane);
        PaneUtils.applyRoundedStyle(teachersInnerPane);
        PaneUtils.applyRoundedStyle(subjectsOuterPane);
        PaneUtils.applyRoundedStyle(subjectsInnerPane);
    }
}
