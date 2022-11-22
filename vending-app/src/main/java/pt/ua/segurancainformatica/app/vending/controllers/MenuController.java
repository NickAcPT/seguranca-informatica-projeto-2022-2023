package pt.ua.segurancainformatica.app.vending.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.conexao.Conexao;
import pt.ua.segurancainformatica.app.vending.model.Menu;
import pt.ua.segurancainformatica.app.vending.model.*;

public class MenuController {

    @FXML
    private Label title;
    @FXML
    private VBox botoesMenu;
    @FXML
    private FlowPane grelha;
    @FXML
    private TableView<ElementoComQuantidade> lista;
    @FXML
    private Button cancelar;
    @FXML
    private TableColumn<Object, Object> preco;
    @FXML
    private TableColumn<Object, Object> produtos;
    @FXML
    private TableColumn<Object, Object> quantidade;

    @Deprecated
    public void initialize() {
        setTitleFont();
        loadButtonFont();

        Entrypoint.getProdutosLista().clear();
        lista.setItems(Entrypoint.getProdutosLista());
        produtos.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        cancelar.setStyle("-fx-background-color: #78120e");

        onBotaoMenuClick();
    }

    private void onBotaozinhoClick(ActionEvent event) {
        Button source = (Button) event.getSource();
        Object userData = source.getUserData();

        if (userData instanceof Produto p) {
            ProdutoComQuantidade pQ = new ProdutoComQuantidade(p, 1);

            addToList(pQ);
        } else if (userData instanceof pt.ua.segurancainformatica.app.vending.model.Menu p) {
            MenuComQuantidade pQ = new MenuComQuantidade(p, 1);

            addToList(pQ);
        }
    }

    private void addToList(ElementoComQuantidade pQ) {
        boolean existeNaLista = false;

        for (ElementoComQuantidade item : Entrypoint.getProdutosLista()) {
            if (item.equals(pQ)) {
                pQ = item;
                existeNaLista = true;
                break;
            }
        }

        if (existeNaLista) {
            pQ.setQuantidade(pQ.getQuantidade() + 1);
        } else {
            Entrypoint.getProdutosLista().add(pQ);
        }
        lista.refresh();
    }

    @FXML
    public void onBotaoMenuClick() {
        grelha.getChildren().clear();

        for (Menu menu : Conexao.getMenus()) {
            createMenuButton(menu);
        }
    }

    @FXML
    protected void onBotaoHamburgerClick() {
        grelha.getChildren().clear();

        for (Produto hamburger : Conexao.getHamburgers()) {
            createProductButton(hamburger);
        }
    }

    @FXML
    protected void onBotaoPizzaClick() {
        grelha.getChildren().clear();

        for (Produto pizza : Conexao.getPizzas()) {
            createProductButton(pizza);
        }
    }

    @FXML
    protected void onBotaoCachorroClick() {
        grelha.getChildren().clear();

        for (Produto cachorro : Conexao.getCachorros()) {
            createProductButton(cachorro);
        }
    }

    @FXML
    protected void onBotaoSandesClick() {
        grelha.getChildren().clear();

        for (Produto sandes : Conexao.getSandes()) {
            createProductButton(sandes);
        }
    }

    @FXML
    protected void onBotaoBebidasClick() {
        grelha.getChildren().clear();

        for (Produto bebida : Conexao.getBebidas()) {
            createProductButton(bebida);
        }
    }

    @FXML
    protected void onBotaoAcompanhamentosClick() {
        grelha.getChildren().clear();

        for (Produto acompanhamento : Conexao.getAcompanhamentos()) {
            createProductButton(acompanhamento);
        }
    }

    @FXML
    public void onBotaoSobremesasClick() {
        grelha.getChildren().clear();

        for (Produto sobremesa : Conexao.getSobremesas()) {
            createProductButton(sobremesa);
        }
    }

    @FXML
    protected void onBotaoRemoverClick() {
        int selectedIndex = lista.getSelectionModel().getSelectedIndex();
        if (-1 == selectedIndex) return;

        ElementoComQuantidade item = Entrypoint.getProdutosLista().get(selectedIndex);
        int novaQtd = item.getQuantidade() - 1;
        item.setQuantidade(novaQtd);

        if (0 >= novaQtd) {
            Entrypoint.getProdutosLista().remove(selectedIndex);
        }
        lista.refresh();
    }

    @FXML
    protected void onBotaoCancelarClick() {
        Entrypoint.loadFile("iniciar_pedido.fxml");

        Entrypoint.getProdutosLista().clear();
    }

    @FXML
    public void onBotaoFinalizarClick() {
        if (Entrypoint.getProdutosLista().isEmpty()) {
            Alert a1 = new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Campos vazios");
            a1.setContentText("Não tem produtos para para fazer o pedido!\nSelecione pelo menos um produto para seguir em frente!");
            a1.setHeaderText(null);
            a1.showAndWait();
        } else {
            Entrypoint.loadFile("confirmar.fxml");
        }
    }

    private void setTitleFont() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Roboto-Bold.ttf"), 32);
        title.setFont(font);
    }

    public void loadButtonFont() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Roboto-Regular.ttf"), 17);
        for (Node child : botoesMenu.getChildren()) {
            if (child instanceof Button button) {
                button.setFont(font);
            }
        }
    }

    private void createProductButton(Produto hamburger) {
        Button btn = new Button();
        btn.setText(hamburger.getNome());
        btn.setUserData(hamburger);
        setDefaultProprieties(btn, hamburger.getFotoImage());
    }

    private void createMenuButton(Menu menu) {
        Button botaozinho = new Button();
        botaozinho.setText(menu.getNome());
        botaozinho.setUserData(menu);
        setDefaultProprieties(botaozinho, menu.getFotoImage());
    }

    private void setDefaultProprieties(Button botaozinho, @Nullable Image foto) {
        botaozinho.setPrefSize(150, 150);
        botaozinho.setPadding(new Insets(0));
        if (foto != null) {
            botaozinho.setGraphic(new ImageView(foto));
            botaozinho.setContentDisplay(ContentDisplay.TOP);
        }
        botaozinho.setId("idBotaozinho");
        botaozinho.setOnAction(this::onBotaozinhoClick);
        botaozinho.wrapTextProperty().setValue(true);
        botaozinho.textAlignmentProperty().setValue(TextAlignment.CENTER);
        grelha.getChildren().add(botaozinho);
    }
}