package ptda.projeto.demo.Controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ptda.projeto.demo.PtdaAplication;
import ptda.projeto.demo.conexao.Conexao;

import java.io.IOException;

public class IniciarPedidoController {

    @FXML
    public void initialize() {
        Conexao.buscarProdutos();
        Conexao.buscarMenus();
    }

    @FXML
    public void onIniciarMouseClicked(Event event) throws IOException {
        PtdaAplication hP = new PtdaAplication();
        hP.changeSceneToMenu("MenuView.fxml");
    }



}
