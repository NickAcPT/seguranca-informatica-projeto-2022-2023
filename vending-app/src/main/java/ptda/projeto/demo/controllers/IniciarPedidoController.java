package ptda.projeto.demo.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import ptda.projeto.demo.conexao.Conexao;

public class IniciarPedidoController {

    @FXML
    public void initialize() {
        Conexao.buscarProdutos();
        Conexao.buscarMenus();
    }

    @FXML
    public void onIniciarMouseClicked() throws IOException {
        Entrypoint.loadFile("MenuView.fxml");
    }


}
