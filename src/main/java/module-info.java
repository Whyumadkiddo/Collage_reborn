module com.example.collage_upgrade {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jdk.hotspot.agent;

    opens com.example.collage_upgrade to javafx.fxml;
    exports com.example.collage_upgrade;
    exports com.example.collage_upgrade.util;
    opens com.example.collage_upgrade.util to javafx.fxml;
    exports com.example.collage_upgrade.fond;
    opens com.example.collage_upgrade.fond to javafx.fxml;
    exports com.example.collage_upgrade.hours_in_week;
    opens com.example.collage_upgrade.hours_in_week to javafx.fxml;
    exports com.example.collage_upgrade.other;
    opens com.example.collage_upgrade.other to javafx.fxml;


}