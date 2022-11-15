package ptda.projeto.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;


@Deprecated(forRemoval = true)
public class PtdaAplication extends Application {

    private static Stage stg;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        primaryStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(PtdaAplication.class.getResource("login_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("APP");
        primaryStage.setScene(scene);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }


}