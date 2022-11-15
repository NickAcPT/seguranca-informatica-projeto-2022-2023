package ptda.projeto.demo.Controllers;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import pt.ua.segurancainformatica.app.vending.Entrypoint;

import java.io.IOException;

public class PedidoConcluidoController {

    @javafx.fxml.FXML
    public void onPedidoMouseClicked() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");
    }

}
