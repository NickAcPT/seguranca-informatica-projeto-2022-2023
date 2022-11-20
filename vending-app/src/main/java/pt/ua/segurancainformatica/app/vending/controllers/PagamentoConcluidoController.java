package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

public class PagamentoConcluidoController {

    @FXML
    public void onPagamentoMouseClicked() {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }
}
