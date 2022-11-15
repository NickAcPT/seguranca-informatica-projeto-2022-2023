package pt.ua.segurancainformatica.app.vending.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;

public class FinalizarController {

    @FXML
    public void pagaMaquina() {
        Entrypoint.loadFile("Contribuinte.fxml");
    }

    @FXML
    public void pagaCaixa() {
        Conexao.guardaPedido(null);
        Entrypoint.loadFile("PedidoConcluido.fxml");
    }

}