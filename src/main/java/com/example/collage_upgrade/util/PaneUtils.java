package com.example.collage_upgrade.util;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PaneUtils {

    // Метод для полного закругления Pane
    public static void applyRoundedStyle(Pane pane) {
        if (pane == null) {
            return; // или выбросить исключение, если это уместно
        }

        // Используем слушателей для обновления clip при изменении размеров
        pane.widthProperty().addListener((obs, oldVal, newVal) -> updateClip(pane));
        pane.heightProperty().addListener((obs, oldVal, newVal) -> updateClip(pane));

        // Инициализируем clip
        updateClip(pane);
    }

    private static void updateClip(Pane pane) {
        Rectangle clip = new Rectangle();
        clip.setWidth(pane.getWidth());
        clip.setHeight(pane.getHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        pane.setClip(clip);
    }

}
