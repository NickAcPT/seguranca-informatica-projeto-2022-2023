package pt.ua.segurancainformatica.app.vending;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.FlatAlert;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.app.vending.model.ElementoComQuantidade;
import pt.ua.segurancainformatica.licensing.common.LicensingCommon;
import pt.ua.segurancainformatica.licensing.lib.LicensingAlertor;
import pt.ua.segurancainformatica.licensing.lib.LicensingException;
import pt.ua.segurancainformatica.licensing.lib.LicensingLibrary;

import java.io.IOException;

public class Entrypoint extends Application {

    private static final ObservableList<ElementoComQuantidade> produtosLista = FXCollections.observableArrayList();

    @Nullable
    private static Stage primaryStage;

    public static ObservableList<ElementoComQuantidade> getProdutosLista() {
        return produtosLista;
    }

    public static void main(String[] args) throws Exception {
        var alertor = new VendingLicensingAlertor();
        try {
            var information = LicensingCommon.getApplicationInformation();
            LicensingLibrary.getInstance().init(
                    information.name(),
                    information.version(),
                    alertor
            );

            Application.launch();
        } catch (LicensingException e) {
            alertor.showLicensingAlert(e.getMessage());
            e.printStackTrace();
        } finally {
            LicensingLibrary.getInstance().close();
        }

    }

    public static @Nullable ButtonType showAlert(Alert.AlertType type, String title, String content, ButtonType... buttons) {
        FlatAlert alert = new FlatAlert(type, title, buttons);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(alert.getDialogPane().getScene());
        alert.setContentText(content);
        return alert.showAndWait().orElse(null);
    }

    public static void loadFile(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Entrypoint.class.getResource("/fxml/" + fxml));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
            if (Entrypoint.primaryStage != null) {
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
    public void start(Stage primaryStage) {
        Entrypoint.primaryStage = primaryStage;
        loadFile("login_screen.fxml");
    }

    static class VendingLicensingAlertor implements LicensingAlertor {
        @Override
        public void showLicensingAlert(String message) {
            showAlert(Alert.AlertType.INFORMATION, "Licenciamento", message, ButtonType.OK);
        }

        @Override
        public boolean showYesNoAlert(String title, String message) {
            return showAlert(Alert.AlertType.INFORMATION, title, message, ButtonType.YES, ButtonType.NO) == ButtonType.YES;
        }
    }
}
