package pt.ua.segurancainformatica.app.vending.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class PedidoConcluidoController {

    @FXML
    public void onPedidoMouseClicked() {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
