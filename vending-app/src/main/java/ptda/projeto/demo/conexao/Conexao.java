package ptda.projeto.demo.conexao;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import ptda.projeto.demo.PtdaAplication;
import ptda.projeto.demo.Tabelas.*;

public class Conexao {

    private static final String CONNECTION_IP = "estga-dev.clients.ua.pt";
    private static final String CONNECTION_DB = "PTDA_BD_3";

    private static Connection conection = null;
    private static Map<Integer, Produto> allProdutos = null;
    private static Map<Integer, Menu> allMenus = null;

    public static Collection<Produto> getProdutos() {
        if (allProdutos == null) {
            buscarProdutos();
        }
        return allProdutos.values();
    }

    public static void buscarProdutos() {
    }

    private static Produto getProdutoFromResultSet(ResultSet set) throws SQLException {
        int id_produto = set.getInt("id_produto");
        String nome = set.getString("nome");
        String genero = set.getString("genero");
        String foto = set.getString("foto");
        double preco = Math.round(set.getDouble("preco") * 100) / 100d;

        return new Produto(id_produto, nome, genero, foto, preco);
    }

    private static Menu getMenuFromResultSet(ResultSet set) throws SQLException {
        int id_menu = set.getInt("id_menu");
        String nome = set.getString("nome");
        String genero = set.getString("genero");
        String detalhes = set.getString("detalhes");
        String foto = set.getString("foto");
        double preco = Math.round(set.getDouble("preco") * 100) / 100d;

        return new Menu(id_menu, nome, preco, foto, detalhes, genero, new ArrayList<>());
    }

    public static Collection<Menu> getMenus() {
        return List.of(new Menu(0, "Teste", 2.0, "https://static.wikia.nocookie.net/meme/images/0/07/Amogus_Template.png/revision/latest?cb=20210308145830", "Teste", "Menus",
                List.of(new Produto(0, "teste 12", "Pizza", "https://static.wikia.nocookie.net/meme/images/0/07/Amogus_Template.png/revision/latest?cb=20210308145830", 2.0))));
    }

    public static void buscarMenus() {
    }

    public static ArrayList<Produto> getHamburgers() {
        return getProdutoByGenero("hamburger");
    }

    public static ArrayList<Produto> getPizzas() {
        return getProdutoByGenero("pizza");
    }

    public static ArrayList<Produto> getSandes() {
        return getProdutoByGenero("sandes");
    }

    public static ArrayList<Produto> getCachorros() {
        return getProdutoByGenero("cachorro");
    }

    public static ArrayList<Produto> getBebidas() {
        return getProdutoByGenero("bebida");
    }

    public static ArrayList<Produto> getSobremesas() {
        return getProdutoByGenero("sobremesas");
    }

    public static ArrayList<Produto> getAcompanhamentos() {
        return getProdutoByGenero("acompanhamentos");
    }

    private static ArrayList<Produto> getProdutoByGenero(String genero) {
        if (allProdutos == null) {
            buscarProdutos();
        }
        return allProdutos.values().stream().filter(produto -> produto.getGenero().equals(genero))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void inserirPedido(Pedido pedido) throws SQLException {

        System.out.println(pedido.getIdFatura().toString());
        System.out.println(pedido.getData().toString());
        System.out.println(pedido.getPreco());
        System.out.println(pedido.getContribuinte());

        for (ProdutoComQuantidade produto : pedido.getProdutos()) {
            inserirPedidoProduto(pedido, produto);
        }
        for (MenuComQuantidade menu : pedido.getMenus()) {
            inserirPedidoMenu(pedido, menu);
        }
    }

    private static void inserirPedidoMenu(Pedido pedido, MenuComQuantidade menu) {
        //     private int idPedido;
        //    private UUID idFatura;
        //    private ArrayList<ProdutoComQuantidade> produtos;
        //    private ArrayList<MenuComQuantidade> menus;
        //    private Instant data;
        //    private String contribuinte;
        //    private double preco;
        System.out.println("Inserir pedido menu " + pedido + " " + menu);
    }

    private static void inserirPedidoProduto(Pedido pedido, ProdutoComQuantidade produto) {
        System.out.println(pedido.getIdPedido());
        System.out.println(produto.getProduto().getId_produto());
        System.out.println(produto.getQuantidade());
    }

    public static void guardaPedido(String contribuinte) {
        double total = PtdaAplication.getProdutosLista().stream()
                .mapToDouble(p -> p.getPreco() * p.getQuantidade())
                .sum();

        Pedido pedido = new Pedido(UUID.randomUUID(), contribuinte, total);
        for (ProdutoComQuantidade produtoComQuantidade : PtdaAplication.getProdutosLista()) {
            if (produtoComQuantidade instanceof MenuComQuantidade) {
                MenuComQuantidade mcq = (MenuComQuantidade) produtoComQuantidade;
                pedido.getMenus().add(mcq);
            } else {
                pedido.getProdutos().add(produtoComQuantidade);
            }
        }

        try {
            inserirPedido(pedido);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
