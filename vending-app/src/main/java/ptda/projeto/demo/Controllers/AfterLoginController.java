package ptda.projeto.demo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

import java.io.IOException;

public class AfterLoginController {

    @FXML
    private Button logout;

    public void userLogOut(ActionEvent event) throws IOException {
        Entrypoint.loadFile("login_screen.fxml");
    }

}