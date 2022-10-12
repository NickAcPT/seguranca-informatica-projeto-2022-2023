package pt.ua.segurancainformatica.app.vending;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

public class Entrypoint extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Nullable
    private static Stage primaryStage = null;

    public static void loadFile(String fxml) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Entrypoint.class.getResource("/" + fxml));
        var scene = new javafx.scene.Scene(fxmlLoader.load());
        if (primaryStage != null) {
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Entrypoint.primaryStage = primaryStage;
        loadFile("Teste.fxml");
    }
}
