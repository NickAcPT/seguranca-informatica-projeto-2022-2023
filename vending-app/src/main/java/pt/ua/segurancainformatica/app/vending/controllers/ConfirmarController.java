package pt.ua.segurancainformatica.app.vending.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.tables.ElementoComQuantidade;
import pt.ua.segurancainformatica.app.vending.tables.ProdutoComQuantidade;

public class ConfirmarController {

    @FXML
    public TableColumn<Object, Object> quantidade;
    @FXML
    public Label totalPagarLabel;
    @FXML
    private TableView<ElementoComQuantidade> lista;
    @FXML
    private TableColumn<Object, Object> preco;
    @FXML
    private TableColumn<Object, Object> produtos;

    @Deprecated
    public void initialize() {

        this.lista.setItems(Entrypoint.getProdutosLista());
        this.produtos.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        this.quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        final double total = Entrypoint.getProdutosLista().stream()
                .mapToDouble(p -> p.getPreco() * p.getQuantidade())
                .sum();

        this.totalPagarLabel.setText(String.format("%.2f", total) + "€");

    }

    @FXML
    public void onConfirmarButtonClick() throws IOException {
        Entrypoint.loadFile("finalizar.fxml");
    }

    @FXML
    public void onVoltarButtonClick() throws IOException {
        Entrypoint.loadFile("MenuView.fxml");
    }
}
