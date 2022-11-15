package ptda.projeto.demo.controllers;

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
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import ptda.projeto.demo.tables.Menu;
import ptda.projeto.demo.tables.MenuComQuantidade;
import ptda.projeto.demo.tables.Produto;
import ptda.projeto.demo.tables.ProdutoComQuantidade;
import ptda.projeto.demo.conexao.Conexao;

import java.io.IOException;

public class MenuController {

    @FXML
    private Label title;
    @FXML
    private VBox botoesMenu;
    @FXML
    private FlowPane grelha;
    @FXML
    private TableView lista;
    @FXML
    private Button cancelar;
    @FXML
    private TableColumn preco;
    @FXML
    private TableColumn produtos;
    @FXML
    private TableColumn quantidade;

    @Deprecated
    public void initialize() {
        fonteTitulo();
        fonteBotoes();

        lista.setItems(Entrypoint.getProdutosLista());
        produtos.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        cancelar.setStyle("-fx-background-color: #78120e");
    }

    private void onBotaozinhoClick(ActionEvent event) {
        Button source = (Button) event.getSource();
        Object userData = source.getUserData();

        if (userData instanceof Produto) {
            Produto p = (Produto) userData;
            ProdutoComQuantidade pQ = new ProdutoComQuantidade(p, 1);

            adicionarNaLista(pQ);
        } else if (userData instanceof Menu) {
            Menu p = (Menu) userData;
            MenuComQuantidade pQ = new MenuComQuantidade(p, 1);

            adicionarNaLista(pQ);
        }
    }

    private void adicionarNaLista(ProdutoComQuantidade pQ) {
        boolean existeNaLista = false;

        for (Object item : Entrypoint.getProdutosLista()) {
            if (item instanceof ProdutoComQuantidade) {
                if (item.equals(pQ)) {
                    pQ = (ProdutoComQuantidade) item;
                    existeNaLista = true;
                    break;
                }
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
            criarBotaozinho(menu);
        }
    }

    @FXML
    protected void onBotaoHamburgerClick() {
        grelha.getChildren().clear();

        for (Produto hamburger : Conexao.getHamburgers()) {
            criarBotaozinho(hamburger);
        }
    }

    @FXML
    protected void onBotaoPizzaClick() {
        grelha.getChildren().clear();

        for (Produto pizza : Conexao.getPizzas()) {
            criarBotaozinho(pizza);
        }
    }

    @FXML
    protected void onBotaoCachorroClick() {
        grelha.getChildren().clear();

        for (Produto cachorro : Conexao.getCachorros()) {
            criarBotaozinho(cachorro);
        }
    }

    @FXML
    protected void onBotaoSandesClick() {
        grelha.getChildren().clear();

        for (Produto sandes : Conexao.getSandes()) {
            criarBotaozinho(sandes);
        }
    }

    @FXML
    protected void onBotaoBebidasClick() {
        grelha.getChildren().clear();

        for (Produto bebida : Conexao.getBebidas()) {
            criarBotaozinho(bebida);
        }
    }

    @FXML
    protected void onBotaoAcompanhamentosClick() {
        grelha.getChildren().clear();

        for (Produto acompanhamento : Conexao.getAcompanhamentos()) {
            criarBotaozinho(acompanhamento);
        }
    }

    @FXML
    public void onBotaoSobremesasClick() {
        grelha.getChildren().clear();

        for (Produto sobremesa : Conexao.getSobremesas()) {
            criarBotaozinho(sobremesa);
        }
    }

    @FXML
    protected void onBotaoRemoverClick() {
        int selectedIndex = lista.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) return;

        Object item = Entrypoint.getProdutosLista().get(selectedIndex);
        if (item instanceof ProdutoComQuantidade) {
            ProdutoComQuantidade produtoComQuantidade = (ProdutoComQuantidade) item;
            int novaQtd = produtoComQuantidade.getQuantidade() - 1;
            produtoComQuantidade.setQuantidade(novaQtd);

            if (novaQtd <= 0) {

                Entrypoint.getProdutosLista().remove(selectedIndex);
            }
            lista.refresh();
        }

    }

    @FXML
    protected void onBotaoCancelarClick() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");

        Entrypoint.getProdutosLista().clear();

    }

    @FXML
    public void onBotaoFinalizarClick() throws IOException {

        if (Entrypoint.getProdutosLista().isEmpty()){
            Alert a1 = new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Campos vazios");
            a1.setContentText("Não tem produtos para para fazer o pedido!\nSelecione pelo menos um produto para seguir em frente!");
            a1.setHeaderText(null);
            a1.showAndWait();
        }else {
            Entrypoint.loadFile("confirmar.fxml");
        }
    }

    private void fonteTitulo() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Roboto-Bold.ttf"), 32);
        title.setFont(font);
    }

    public void fonteBotoes() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Roboto-Regular.ttf"), 17);
        for (Node child : botoesMenu.getChildren()) {
            if (child instanceof Button) {
                Button button = ((Button) child);
                button.setFont(font);
            }
        }
    }

    private void criarBotaozinho(Produto hamburger) {
        Button botaozinho = new Button();
        botaozinho.setText(hamburger.getNome());
        botaozinho.setUserData(hamburger);
        setDefaultProprieties(botaozinho, hamburger.getFotoImage());
    }

    private void criarBotaozinho(Menu menu) {
        Button botaozinho = new Button();
        botaozinho.setText(menu.getNome());
        botaozinho.setUserData(menu);
        setDefaultProprieties(botaozinho, menu.getFotoImage());
    }

    private void setDefaultProprieties(Button botaozinho, Image foto) {
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