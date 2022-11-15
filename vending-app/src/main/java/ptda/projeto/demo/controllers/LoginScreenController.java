package ptda.projeto.demo.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class LoginScreenController {

    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void userLogin() throws IOException {
        if (this.username.getText().isEmpty() || this.password.getText().isEmpty()) {
            Entrypoint.showAlert(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos!", ButtonType.OK);
        } else {
            Entrypoint.loadFile("iniciar_pedido.fxml");
        }
    }
}
