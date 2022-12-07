package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.VendingApplication;

public class IniciarPedidoController {

    @FXML
    public void onIniciarMouseClicked() {
        VendingApplication.loadFile("menu_view.fxml");
    }
}
