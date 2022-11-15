package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;

public class IniciarPedidoController {

    @FXML
    public void onIniciarMouseClicked() {
        Entrypoint.loadFile("menu_view.fxml");
    }
}
