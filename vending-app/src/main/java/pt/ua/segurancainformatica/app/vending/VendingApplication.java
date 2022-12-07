package pt.ua.segurancainformatica.app.vending;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
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

public class VendingApplication extends Application {

    private static final ObservableList<ElementoComQuantidade> produtosLista = FXCollections.observableArrayList();

    @Nullable
    private static Stage primaryStage;

    public static ObservableList<ElementoComQuantidade> getProdutosLista() {
        return produtosLista;
    }

    public static @Nullable ButtonType showAlert(Alert.AlertType type, String title, String content, ButtonType... buttons) {
        FlatAlert alert = new FlatAlert(type, title, buttons);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(alert.getDialogPane().getScene());

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(600);

        alert.setContentText(content);

        return alert.showAndWait().orElse(null);
    }

    public static void loadFile(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(VendingApplication.class.getResource("/fxml/" + fxml));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
            if (VendingApplication.primaryStage != null) {
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

    public static void main(String[] args) {
        Application.launch(args);
    }

    private static void run(Stage primaryStage) {
        VendingApplication.primaryStage = primaryStage;
        loadFile("login_screen.fxml");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var alertor = new VendingApplication.VendingLicensingAlertor();
        try {
            var information = LicensingCommon.getApplicationInformation();

            if (LicensingLibrary.getInstance().init(information.name(), information.version(), alertor))
                run(primaryStage);
        } catch (LicensingException e) {
            alertor.showLicensingAlert(e.getMessage());
            e.printStackTrace();
        } finally {
            LicensingLibrary.getInstance().close();
        }
    }

    static class VendingLicensingAlertor implements LicensingAlertor {
        @Override
        public void showLicensingAlert(String message) {
            showAlert(Alert.AlertType.INFORMATION, "Licenciamento", message, ButtonType.OK);
        }

        @Override
        public boolean showYesNoAlert(String title, String message) {
            try {
                return showAlert(Alert.AlertType.CONFIRMATION, title, message, ButtonType.YES, ButtonType.NO) == ButtonType.YES;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
