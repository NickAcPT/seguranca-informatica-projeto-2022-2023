package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;

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
            Entrypoint.showAlert(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos!", ButtonType.OK);
        } else {
            Entrypoint.loadFile("iniciar_pedido.fxml");
        }
    }
}