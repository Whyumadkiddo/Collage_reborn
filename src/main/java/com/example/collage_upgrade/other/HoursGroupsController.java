package com.example.collage_upgrade.other;

import com.example.collage_upgrade.util.PaneUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class HoursGroupsController {

    @FXML
    private Pane mainPane1;

    @FXML
    private Pane mainInnerPane1;

    @FXML
    private Pane subGroupPane1;

    @FXML
    private Pane subGroupInnerPane1;

    @FXML
    private Pane subGroupProgrammersPane1;

    @FXML
    private Pane subGroupProgrammersInnerPane1;

    @FXML
    private Pane ppSpecPane1;

    @FXML
    private Pane ppSpecInnerPane1;

    @FXML
    private Pane upSpecPane1;

    @FXML
    private Pane upSpecInnerPane1;

    @FXML
    private Pane subGroupUpPane1;

    @FXML
    private Pane subGroupUpInnerPane1;

    @FXML
    private Pane hoursByPlanPane1;

    @FXML
    private Pane hoursByPlanInnerPane1;

    @FXML
    private Pane LocatePane;

    @FXML
    private Pane LocateGroupPane;

    @FXML
    public void initialize() {
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
        PaneUtils.applyRoundedStyle(LocatePane);
        PaneUtils.applyRoundedStyle(LocateGroupPane);
    }
}
