package ptda.projeto.demo.conexao;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import ptda.projeto.demo.tables.*;

public class Conexao {

    private static Map<Integer, Produto> allProdutos = null;

    public static void buscarProdutos() {
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
        System.out.println(pedido.getIdFatura());
        System.out.println(produto.getProduto().getId_produto());
        System.out.println(produto.getQuantidade());
    }

    public static void guardaPedido(String contribuinte) {

        double total = Entrypoint.getProdutosLista().stream()
                .mapToDouble(p -> p.getPreco() * p.getQuantidade())
                .sum();

        Pedido pedido = new Pedido(UUID.randomUUID(), contribuinte, total);

        for (ProdutoComQuantidade produtoComQuantidade : Entrypoint.getProdutosLista()) {
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
