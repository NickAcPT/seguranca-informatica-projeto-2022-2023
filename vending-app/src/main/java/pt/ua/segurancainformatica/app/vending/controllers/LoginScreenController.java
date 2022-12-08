package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pt.ua.segurancainformatica.app.vending.VendingApplication;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;
import pt.ua.segurancainformatica.licensing.lib.LicensingLibrary;

public class LoginScreenController {

    @FXML
    public void initialize() {
        Conexao.buscarProdutos();
        Conexao.buscarMenus();
    }

    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void userLogin() {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            VendingApplication.showAlert(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos!", ButtonType.OK);
        } else {
            VendingApplication.loadFile("iniciar_pedido.fxml");
        }
    }

    public void showInfoLicense(ActionEvent actionEvent) {
        LicensingLibrary.getInstance().showLicenseInfo();
    }
}
