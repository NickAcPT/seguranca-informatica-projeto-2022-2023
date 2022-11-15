package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;

public class IniciarPedidoController {

    @FXML
    public void initialize() {
        Conexao.buscarProdutos();
        Conexao.buscarMenus();
    }

    @FXML
    public void onIniciarMouseClicked() {
        Entrypoint.loadFile("MenuView.fxml");
    }


}
