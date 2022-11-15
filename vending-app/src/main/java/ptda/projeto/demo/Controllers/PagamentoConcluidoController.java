package ptda.projeto.demo.Controllers;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

import java.io.IOException;

public class PagamentoConcluidoController {

    @javafx.fxml.FXML
    public void onPagamentoMouseClicked() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
