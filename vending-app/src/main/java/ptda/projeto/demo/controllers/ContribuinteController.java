package ptda.projeto.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import ptda.projeto.demo.conexao.Conexao;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class ContribuinteController {

    @FXML
    private TextField txtContribuinte;

    @FXML
    private Label label;

    @FXML
    public void onBotaoAvancarClick() throws IOException {

        String contribuinte = "Consumidor Final";

        if (txtContribuinte.getText() == null || txtContribuinte.getText().equals("")){
            txtContribuinte.setText(contribuinte);
            Entrypoint.loadFile("PagamentoConcluido.fxml");
        } else {

            contribuinte = txtContribuinte.getText();

            try {
                Integer.parseInt(txtContribuinte.getText());
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
    public void onBotaoVoltarClick() throws IOException {
        Entrypoint.loadFile("finalizar.fxml");
    }
}