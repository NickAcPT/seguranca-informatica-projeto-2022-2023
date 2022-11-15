package ptda.projeto.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import ptda.projeto.demo.conexao.Conexao;

import java.io.IOException;

public class FinalizarController {

    @FXML
    public void pagaMaquina() throws IOException {
        Entrypoint.loadFile("Contribuinte.fxml");
    }

    @FXML
    public void pagaCaixa() throws IOException {
        Conexao.guardaPedido(null);
        Entrypoint.loadFile("PedidoConcluido.fxml");
    }

}