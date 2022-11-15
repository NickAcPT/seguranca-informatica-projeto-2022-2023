package pt.ua.segurancainformatica.app.vending.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class PagamentoConcluidoController {

    @FXML
    public void onPagamentoMouseClicked() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
