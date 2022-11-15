package ptda.projeto.demo.Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.styles.jmetro.*;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class LoginScreenController {

    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void userLogin() throws IOException {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            Entrypoint.showAlert(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos!", ButtonType.OK);
        } else {
            Entrypoint.loadFile("iniciar_pedido.fxml");
        }
    }
}
