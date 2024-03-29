package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.ua.segurancainformatica.app.vending.VendingApplication;
import pt.ua.segurancainformatica.app.vending.model.ElementoComQuantidade;

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
        lista.setItems(VendingApplication.getProdutosLista());
        produtos.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        double total = VendingApplication.getProdutosLista().stream()
                .mapToDouble(p -> p.getPreco() * p.getQuantidade())
                .sum();

        totalPagarLabel.setText(String.format("%.2f", total) + "€");
    }

    @FXML
    public void onConfirmarButtonClick() {
        VendingApplication.loadFile("finalizar.fxml");
    }

    @FXML
    public void onVoltarButtonClick() {
        VendingApplication.loadFile("menu_view.fxml");
    }
}
