package ptda.projeto.demo.controllers;

import java.io.IOException;
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
import ptda.projeto.demo.conexao.Conexao;
import ptda.projeto.demo.tables.Menu;
import ptda.projeto.demo.tables.*;

public class MenuController {

    @FXML
    private Label title;
    @FXML
    private VBox botoesMenu;
    @FXML
    private FlowPane grelha;
    @FXML
    private TableView<ProdutoComQuantidade> lista;
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
        this.fonteTitulo();
        this.fonteBotoes();

        this.lista.setItems(Entrypoint.getProdutosLista());
        this.produtos.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        this.quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.cancelar.setStyle("-fx-background-color: #78120e");
    }

    private void onBotaozinhoClick(final ActionEvent event) {
        final Button source = (Button) event.getSource();
        final Object userData = source.getUserData();

        if (userData instanceof Produto p) {
            final ProdutoComQuantidade pQ = new ProdutoComQuantidade(p, 1);

            this.adicionarNaLista(pQ);
        } else if (userData instanceof Menu p) {
            final MenuComQuantidade pQ = new MenuComQuantidade(p, 1);

            this.adicionarNaLista(pQ);
        }
    }

    private void adicionarNaLista(ProdutoComQuantidade pQ) {
        boolean existeNaLista = false;

        for (final ProdutoComQuantidade item : Entrypoint.getProdutosLista()) {
            if (item instanceof ProdutoComQuantidade) {
                if (item.equals(pQ)) {
                    pQ = item;
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
        this.lista.refresh();
    }

    @FXML
    public void onBotaoMenuClick() {
        this.grelha.getChildren().clear();

        for (final Menu menu : Conexao.getMenus()) {
            this.criarBotaozinho(menu);
        }
    }

    @FXML
    protected void onBotaoHamburgerClick() {
        this.grelha.getChildren().clear();

        for (final Produto hamburger : Conexao.getHamburgers()) {
            this.criarBotaozinho(hamburger);
        }
    }

    @FXML
    protected void onBotaoPizzaClick() {
        this.grelha.getChildren().clear();

        for (final Produto pizza : Conexao.getPizzas()) {
            this.criarBotaozinho(pizza);
        }
    }

    @FXML
    protected void onBotaoCachorroClick() {
        this.grelha.getChildren().clear();

        for (final Produto cachorro : Conexao.getCachorros()) {
            this.criarBotaozinho(cachorro);
        }
    }

    @FXML
    protected void onBotaoSandesClick() {
        this.grelha.getChildren().clear();

        for (final Produto sandes : Conexao.getSandes()) {
            this.criarBotaozinho(sandes);
        }
    }

    @FXML
    protected void onBotaoBebidasClick() {
        this.grelha.getChildren().clear();

        for (final Produto bebida : Conexao.getBebidas()) {
            this.criarBotaozinho(bebida);
        }
    }

    @FXML
    protected void onBotaoAcompanhamentosClick() {
        this.grelha.getChildren().clear();

        for (final Produto acompanhamento : Conexao.getAcompanhamentos()) {
            this.criarBotaozinho(acompanhamento);
        }
    }

    @FXML
    public void onBotaoSobremesasClick() {
        this.grelha.getChildren().clear();

        for (final Produto sobremesa : Conexao.getSobremesas()) {
            this.criarBotaozinho(sobremesa);
        }
    }

    @FXML
    protected void onBotaoRemoverClick() {
        final int selectedIndex = this.lista.getSelectionModel().getSelectedIndex();
        if (-1 == selectedIndex) return;

        final ProdutoComQuantidade item = Entrypoint.getProdutosLista().get(selectedIndex);
        if (item instanceof ProdutoComQuantidade) {
            final ProdutoComQuantidade produtoComQuantidade = item;
            final int novaQtd = produtoComQuantidade.getQuantidade() - 1;
            produtoComQuantidade.setQuantidade(novaQtd);

            if (0 >= novaQtd) {

                Entrypoint.getProdutosLista().remove(selectedIndex);
            }
            this.lista.refresh();
        }

    }

    @FXML
    protected void onBotaoCancelarClick() throws IOException {
        Entrypoint.loadFile("iniciar_pedido.fxml");

        Entrypoint.getProdutosLista().clear();

    }

    @FXML
    public void onBotaoFinalizarClick() throws IOException {

        if (Entrypoint.getProdutosLista().isEmpty()) {
            final Alert a1 = new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Campos vazios");
            a1.setContentText(
                    "Não tem produtos para para fazer o pedido!\nSelecione pelo menos um produto para seguir em frente!");
            a1.setHeaderText(null);
            a1.showAndWait();
        } else {
            Entrypoint.loadFile("confirmar.fxml");
        }
    }

    private void fonteTitulo() {
        final Font font = Font.loadFont(this.getClass().getResourceAsStream("/Roboto-Bold.ttf"), 32);
        this.title.setFont(font);
    }

    public void fonteBotoes() {
        final Font font = Font.loadFont(this.getClass().getResourceAsStream("/Roboto-Regular.ttf"), 17);
        for (final Node child : this.botoesMenu.getChildren()) {
            if (child instanceof Button button) {
                button.setFont(font);
            }
        }
    }

    private void criarBotaozinho(final Produto hamburger) {
        final Button botaozinho = new Button();
        botaozinho.setText(hamburger.getNome());
        botaozinho.setUserData(hamburger);
        this.setDefaultProprieties(botaozinho, hamburger.getFotoImage());
    }

    private void criarBotaozinho(final Menu menu) {
        final Button botaozinho = new Button();
        botaozinho.setText(menu.getNome());
        botaozinho.setUserData(menu);
        this.setDefaultProprieties(botaozinho, menu.getFotoImage());
    }

    private void setDefaultProprieties(final Button botaozinho, final Image foto) {
        botaozinho.setPrefSize(150, 150);
        botaozinho.setPadding(new Insets(0));
        if (null != foto) {
            botaozinho.setGraphic(new ImageView(foto));
            botaozinho.setContentDisplay(ContentDisplay.TOP);
        }
        botaozinho.setId("idBotaozinho");
        botaozinho.setOnAction(this::onBotaozinhoClick);
        botaozinho.wrapTextProperty().setValue(true);
        botaozinho.textAlignmentProperty().setValue(TextAlignment.CENTER);
        this.grelha.getChildren().add(botaozinho);
    }
}