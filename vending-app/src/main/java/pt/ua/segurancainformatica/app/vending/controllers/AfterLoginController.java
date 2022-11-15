package pt.ua.segurancainformatica.app.vending.controllers;

import java.io.IOException;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class AfterLoginController {

    public void userLogOut() {
        Entrypoint.loadFile("login_screen.fxml");
    }

}