package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class PedidoConcluidoController {

    @FXML
    public void onPedidoMouseClicked() {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
