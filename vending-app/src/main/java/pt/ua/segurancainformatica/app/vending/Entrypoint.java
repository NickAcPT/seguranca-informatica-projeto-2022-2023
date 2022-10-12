package pt.ua.segurancainformatica.app.vending;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Entrypoint extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load Teste.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(Entrypoint.class.getResource("/Teste.fxml"));
        // Create the scene
        var scene = new javafx.scene.Scene(fxmlLoader.load(), 300, 275);
        // Set the scene
        primaryStage.setScene(scene);
        // Show the stage
        primaryStage.show();
    }
}
