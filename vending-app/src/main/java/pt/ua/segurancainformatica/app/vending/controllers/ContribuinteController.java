package pt.ua.segurancainformatica.app.vending.controllers;

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
    public void onBotaoAvancarClick() {

        String contribuinte = "Consumidor Final";

        if (null == txtContribuinte.getText() || "".equals(this.txtContribuinte.getText())) {
            txtContribuinte.setText(contribuinte);
            Entrypoint.loadFile("pagamento_concluido.fxml");
        } else {

            contribuinte = txtContribuinte.getText();

            try {
                Integer.parseInt(txtContribuinte.getText());
                Entrypoint.loadFile("pagamento_concluido.fxml");
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
    public void onBotaoVoltarClick() {
        Entrypoint.loadFile("finalizar.fxml");
    }
}