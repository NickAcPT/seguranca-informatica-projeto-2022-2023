package ptda.projeto.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.jetbrains.annotations.NotNull;
import ptda.projeto.demo.Tabelas.ProdutoComQuantidade;

import java.io.IOException;


@Deprecated(forRemoval = true)
public class PtdaAplication extends Application {

    private static Stage stg;
    private static ObservableList<ProdutoComQuantidade> produtosLista;

    public static ObservableList<ProdutoComQuantidade> getProdutosLista() {
        return produtosLista;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        produtosLista = FXCollections.observableArrayList();
        primaryStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(PtdaAplication.class.getResource("login_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("APP");
        primaryStage.setScene(scene);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(scene);
        primaryStage.show();
    }

    public void changeSceneToIniciar(String fxml) throws IOException {
        produtosLista.clear();
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.setWidth(620);
        stg.setHeight(840);
        stg.centerOnScreen();
        pane.setStyle("accent_color: #217346");
    }

    public void changeSceneToMenu(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.setWidth(620);
        stg.setHeight(840);
        stg.centerOnScreen();
        pane.setStyle("accent_color: #217346");
    }

    public void changeSceneToConfirmar(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.setWidth(620);
        stg.setHeight(440);
        stg.centerOnScreen();
        pane.setStyle("accent_color: #217346");
    }

    public void changeSceneToFinalizar(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.setWidth(600);
        stg.setHeight(440);
        stg.centerOnScreen();
        pane.setStyle("accent_color: #217346");
    }

    public void changeSceneToContribuinte (String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.setWidth(600);
        stg.setHeight(440);
        stg.centerOnScreen();
        pane.setStyle("accent_color: #217346");
    }

    public void changeSceneToPagamentoConcluido (String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.setWidth(600);
        stg.setHeight(440);
        stg.centerOnScreen();
        pane.setStyle("accent_color: #217346");
    }

    public static void main(String[] args) throws IOException {
        launch();
    }


}