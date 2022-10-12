package ptda.projeto.demo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ptda.projeto.demo.PtdaAplication;
import ptda.projeto.demo.Tabelas.ProdutoComQuantidade;

import java.io.IOException;

public class ConfirmarController {

    @FXML
    public TableColumn quantidade;
    @FXML
    public Label totalPagarLabel;
    @FXML
    private TableView lista;
    @FXML
    private TableColumn preco;
    @FXML
    private TableColumn produtos;

    @FXML
    private Button voltarButton;
    @FXML
    private Button confirmarButton;

    @Deprecated
    public void initialize() {
        lista.setItems(PtdaAplication.getProdutosLista());
        produtos.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        double total = PtdaAplication.getProdutosLista().stream()
                .mapToDouble(p -> p.getPreco() * p.getQuantidade())
                .sum();

        totalPagarLabel.setText(String.format("%.2f", total) + "€");

    }

    @FXML
    public void onConfirmarButtonClick(ActionEvent actionEvent) throws IOException {
        PtdaAplication hP = new PtdaAplication();
        hP.changeSceneToFinalizar("finalizar.fxml");
    }

    @FXML
    public void onVoltarButtonClick(ActionEvent actionEvent) throws IOException {
        PtdaAplication hP = new PtdaAplication();
        hP.changeSceneToMenu("MenuView.fxml");
    }
}
