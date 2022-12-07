package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.VendingApplication;

public class PedidoConcluidoController {

    @FXML
    public void onPedidoMouseClicked() {
        VendingApplication.loadFile("iniciar_pedido.fxml");
    }
}
