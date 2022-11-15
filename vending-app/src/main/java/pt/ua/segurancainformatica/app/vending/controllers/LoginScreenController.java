package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class LoginScreenController {

    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void userLogin() {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            Entrypoint.showAlert(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos!", ButtonType.OK);
        } else {
            Entrypoint.loadFile("iniciar_pedido.fxml");
        }
    }
}
