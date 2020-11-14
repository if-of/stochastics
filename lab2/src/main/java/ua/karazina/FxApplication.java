package ua.karazina;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        stage.setTitle("Lab1");
        stage.setScene(new Scene(root, 610, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
