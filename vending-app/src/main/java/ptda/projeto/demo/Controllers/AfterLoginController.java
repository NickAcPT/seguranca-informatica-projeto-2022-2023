package ptda.projeto.demo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

import java.io.IOException;

public class AfterLoginController {

    public void userLogOut() throws IOException {
        Entrypoint.loadFile("login_screen.fxml");
    }

}