package pt.ua.segurancainformatica.app.vending.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class PedidoConcluidoController {

    @FXML
    public void onPedidoMouseClicked() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
