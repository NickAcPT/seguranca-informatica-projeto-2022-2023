package ptda.projeto.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PtdaController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


}