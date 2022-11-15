package pt.ua.segurancainformatica.app.vending.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;

public class ContribuinteController {

    @FXML
    private TextField txtContribuinte;

    @FXML
    private Label label;

    @FXML
    public void onBotaoAvancarClick() throws IOException {

        String contribuinte = "Consumidor Final";

        if (txtContribuinte.getText() == null || "".equals(txtContribuinte.getText())) {
            this.txtContribuinte.setText(contribuinte);
            Entrypoint.loadFile("PagamentoConcluido.fxml");
        } else {

            contribuinte = this.txtContribuinte.getText();

            try {
                Integer.parseInt(this.txtContribuinte.getText());
                Entrypoint.loadFile("PagamentoConcluido.fxml");

            } catch (final NumberFormatException e) {
                this.label.setText("Digite apenas números!");
                return;
            } catch (final Exception e) {
                this.label.setText("erro");
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