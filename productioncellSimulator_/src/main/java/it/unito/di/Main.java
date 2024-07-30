package it.unito.di;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        String path = "/fxml/gui.fxml";
        Image icon = new Image("/images/icon.png");

        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setTitle("Production Cell Simulator");
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {

        launch(args);

    }
}