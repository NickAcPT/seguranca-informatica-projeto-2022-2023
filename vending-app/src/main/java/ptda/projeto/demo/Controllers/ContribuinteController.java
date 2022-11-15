package ptda.projeto.demo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import ptda.projeto.demo.PtdaAplication;
import ptda.projeto.demo.conexao.Conexao;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class ContribuinteController {

    @FXML
    private TextField txtContribuinte;

    @FXML
    private Label label;

    int numContribuinte;
    @FXML
    private Button BtnVoltar;
    @FXML
    private Button BtnAvancar1;

    @FXML
    public void onBotaoAvancarClick(ActionEvent actionEvent) throws IOException {

        String contribuinte = "Consumidor Final";

        if (txtContribuinte.getText() == null || txtContribuinte.getText().equals("")){
            txtContribuinte.setText(contribuinte);
            PtdaAplication hP = new PtdaAplication();
            Entrypoint.loadFile("PagamentoConcluido.fxml");
        } else {

            contribuinte = txtContribuinte.getText();

            try {
                numContribuinte = Integer.parseInt(txtContribuinte.getText());
                PtdaAplication hP = new PtdaAplication();
                Entrypoint.loadFile("PagamentoConcluido.fxml");

            } catch (NumberFormatException e) {
                label.setText("Digite apenas números!");
                return;
            } catch (Exception e) {
                label.setText("erro");
                return;
            }
        }

        Conexao.guardaPedido(contribuinte);

    }

    @FXML
    public void onBotaoVoltarClick(ActionEvent actionEvent) throws IOException {
        PtdaAplication hP = new PtdaAplication();
        Entrypoint.loadFile("finalizar.fxml");
    }
}