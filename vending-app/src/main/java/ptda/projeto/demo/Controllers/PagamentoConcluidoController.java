package ptda.projeto.demo.Controllers;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ptda.projeto.demo.PtdaAplication;

import java.io.IOException;

public class PagamentoConcluidoController {

    @javafx.fxml.FXML
    private Label pagamentoLabel1;
    @javafx.fxml.FXML
    private Label pagamentoLabel2;
    @javafx.fxml.FXML
    private AnchorPane pagamentoPane;

    @javafx.fxml.FXML
    public void onPagamentoMouseClicked(Event event) throws IOException {
        PtdaAplication hP = new PtdaAplication();
        hP.changeSceneToIniciar("iniciar_pedido.fxml");
    }

}
