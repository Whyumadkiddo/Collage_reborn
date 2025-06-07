package com.example.collage_upgrade.util;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PaneUtils {

    // Метод для полного закругления Pane
    public static void applyRoundedStyle(Pane pane) {
        Rectangle clip = new Rectangle();
        clip.setWidth(pane.getPrefWidth());
        clip.setHeight(pane.getPrefHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);

        pane.setClip(clip);
    }
}
