package ptda.projeto.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import ptda.projeto.demo.PtdaAplication;
import ptda.projeto.demo.conexao.Conexao;

import java.io.IOException;

public class FinalizarController {

    @FXML
    private Button BtnCancela;

    @FXML
    private Button BtnPagaAqui;

    @FXML
    private Button BtnPagaCaixa;

    @FXML
    public void pagaMaquina() throws IOException {
        PtdaAplication hP = new PtdaAplication();
        Entrypoint.loadFile("Contribuinte.fxml");
    }

    @FXML
    public void pagaCaixa() throws IOException {
        Conexao.guardaPedido(null);
        PtdaAplication hP = new PtdaAplication();
        Entrypoint.loadFile("PedidoConcluido.fxml");
    }

}