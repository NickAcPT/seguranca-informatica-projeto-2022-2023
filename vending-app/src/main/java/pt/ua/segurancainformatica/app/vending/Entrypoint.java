package pt.ua.segurancainformatica.app.vending;

import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.*;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.app.vending.tables.ProdutoComQuantidade;

public class Entrypoint extends Application {


    private static final ObservableList<ProdutoComQuantidade> produtosLista = FXCollections.observableArrayList();

    @Nullable
    private static Stage primaryStage;

    public static ObservableList<ProdutoComQuantidade> getProdutosLista() {
        return Entrypoint.produtosLista;
    }

    public static void main(final String[] args) {
        Application.launch();
    }

    public static void showAlert(final Alert.AlertType type, final String title, final String content, final ButtonType... buttons) {
        final FlatAlert alert = new FlatAlert(type, title, buttons);
        final JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(alert.getDialogPane().getScene());
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void loadFile(final String fxml) {
        final FXMLLoader fxmlLoader = new FXMLLoader(Entrypoint.class.getResource("/fxml/" + fxml));
        final Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
            if (null != primaryStage) {
                final JMetro jMetro = new JMetro(Style.DARK);
                jMetro.setScene(scene);
                scene.getRoot().getStyleClass().add("background");

                Entrypoint.primaryStage.setScene(scene);
                Entrypoint.primaryStage.show();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        Entrypoint.primaryStage = primaryStage;
        Entrypoint.loadFile("login_screen.fxml");
    }
}
