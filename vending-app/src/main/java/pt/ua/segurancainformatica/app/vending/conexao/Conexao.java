package pt.ua.segurancainformatica.app.vending.conexao;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.tables.*;

public enum Conexao {
    ;

    private static final @Nullable Map<Integer, Produto> allProdutos = null;

    public static void buscarProdutos() {
    }

    public static Collection<Menu> getMenus() {
        return List.of(new Menu(0, "Teste", 2.0,
                "https://static.wikia.nocookie.net/meme/images/0/07/Amogus_Template.png/revision/latest?cb=20210308145830",
                "Teste", "Menus",
                List.of(new Produto(0, "teste 12", "Pizza",
                        "https://static.wikia.nocookie.net/meme/images/0/07/Amogus_Template.png/revision/latest?cb=20210308145830",
                        2.0))));
    }

    public static void buscarMenus() {
    }

    public static ArrayList<Produto> getHamburgers() {
        return Conexao.getProdutoByGenero("hamburger");
    }

    public static ArrayList<Produto> getPizzas() {
        return Conexao.getProdutoByGenero("pizza");
    }

    public static ArrayList<Produto> getSandes() {
        return Conexao.getProdutoByGenero("sandes");
    }

    public static ArrayList<Produto> getCachorros() {
        return Conexao.getProdutoByGenero("cachorro");
    }

    public static ArrayList<Produto> getBebidas() {
        return Conexao.getProdutoByGenero("bebida");
    }

    public static ArrayList<Produto> getSobremesas() {
        return Conexao.getProdutoByGenero("sobremesas");
    }

    public static ArrayList<Produto> getAcompanhamentos() {
        return Conexao.getProdutoByGenero("acompanhamentos");
    }

    private static ArrayList<Produto> getProdutoByGenero(final String genero) {
        if (allProdutos == null) {
            Conexao.buscarProdutos();
            return new ArrayList<>();
        }
        return Conexao.allProdutos.values().stream().filter(produto -> produto.getGenero().equals(genero))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void inserirPedido(final Pedido pedido) throws SQLException {

        System.out.println(pedido.getIdFatura().toString());
        System.out.println(pedido.getData().toString());
        System.out.println(pedido.getPreco());
        System.out.println(pedido.getContribuinte());

        for (final ProdutoComQuantidade produto : pedido.getProdutos()) {
            Conexao.inserirPedidoProduto(pedido, produto);
        }
        for (final MenuComQuantidade menu : pedido.getMenus()) {
            Conexao.inserirPedidoMenu(pedido, menu);
        }
    }

    private static void inserirPedidoMenu(final Pedido pedido, final MenuComQuantidade menu) {
        System.out.println("Inserir pedido menu " + pedido + " " + menu);
    }

    private static void inserirPedidoProduto(final Pedido pedido, final ProdutoComQuantidade produto) {
        System.out.println(pedido.getIdFatura());
        System.out.println(produto.getProduto().getId_produto());
        System.out.println(produto.getQuantidade());
    }

    public static void guardaPedido(@Nullable final String contribuinte) {

        final double total = Entrypoint.getProdutosLista().stream()
                .mapToDouble(p -> p.getPreco() * p.getQuantidade())
                .sum();

        final Pedido pedido = new Pedido(UUID.randomUUID(), contribuinte, total);

        for (final ElementoComQuantidade elementoComQuantidade : Entrypoint.getProdutosLista()) {
            if (elementoComQuantidade instanceof MenuComQuantidade mcq) {
                pedido.getMenus().add(mcq);
            } else if (elementoComQuantidade instanceof ProdutoComQuantidade pcq) {
                pedido.getProdutos().add(pcq);
            }
        }

        try {
            Conexao.inserirPedido(pedido);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }
}
