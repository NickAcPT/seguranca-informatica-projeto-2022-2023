package pt.ua.segurancainformatica.app.vending;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.*;
import org.jetbrains.annotations.Nullable;

public class Entrypoint extends Application {

    @Nullable
    private static Stage primaryStage = null;

    public static void main(String[] args) {
        launch();
    }

    public static void showAlert(Alert.AlertType type, String title, String content, ButtonType... buttons) {
        FlatAlert alert = new FlatAlert(type, title, buttons);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(alert.getDialogPane().getScene());
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void loadFile(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Entrypoint.class.getResource("/fxml/" + fxml));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            if (primaryStage != null) {
                JMetro jMetro = new JMetro(Style.DARK);
                jMetro.setScene(scene);
                scene.getRoot().getStyleClass().add("background");

                primaryStage.setScene(scene);
                primaryStage.show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Entrypoint.primaryStage = primaryStage;
        loadFile("login_screen.fxml");
    }
}
