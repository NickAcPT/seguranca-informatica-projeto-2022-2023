package ptda.projeto.demo.Controllers;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

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
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
