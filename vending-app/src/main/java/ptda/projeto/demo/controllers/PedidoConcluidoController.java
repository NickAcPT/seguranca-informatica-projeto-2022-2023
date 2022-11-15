package ptda.projeto.demo.controllers;

import pt.ua.segurancainformatica.app.vending.Entrypoint;

import java.io.IOException;

public class PedidoConcluidoController {

    @javafx.fxml.FXML
    public void onPedidoMouseClicked() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
