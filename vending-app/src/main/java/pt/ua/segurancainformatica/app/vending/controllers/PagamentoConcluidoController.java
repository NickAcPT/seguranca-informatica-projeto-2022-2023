package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.VendingApplication;

public class PagamentoConcluidoController {

    @FXML
    public void onPagamentoMouseClicked() {
        VendingApplication.loadFile("iniciar_pedido.fxml");
    }
}
