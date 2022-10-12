package ptda.projeto.demo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ptda.projeto.demo.PtdaAplication;

import java.io.IOException;

public class AfterLoginController {

    @FXML
    private Button logout;

    public void userLogOut(ActionEvent event) throws IOException {
        PtdaAplication hP = new PtdaAplication();
        hP.changeSceneToMenu("login_screen.fxml");
    }

}