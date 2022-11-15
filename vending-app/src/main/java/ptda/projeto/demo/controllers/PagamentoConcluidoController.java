package ptda.projeto.demo.controllers;

import pt.ua.segurancainformatica.app.vending.Entrypoint;

import java.io.IOException;

public class PagamentoConcluidoController {

    @javafx.fxml.FXML
    public void onPagamentoMouseClicked() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
