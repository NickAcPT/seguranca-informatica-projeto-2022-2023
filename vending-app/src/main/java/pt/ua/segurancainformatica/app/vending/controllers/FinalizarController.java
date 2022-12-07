package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.VendingApplication;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;

public class FinalizarController {

    @FXML
    public void pagaMaquina() {
        VendingApplication.loadFile("Contribuinte.fxml");
    }

    @FXML
    public void pagaCaixa() {
        Conexao.guardaPedido(null);
        VendingApplication.loadFile("pedido_concluido.fxml");
    }
}