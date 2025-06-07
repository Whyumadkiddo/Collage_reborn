package com.example.collage_upgrade.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneUtils {

    public static void openNewWindow(String fxmlFile, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.show();
    }
}
