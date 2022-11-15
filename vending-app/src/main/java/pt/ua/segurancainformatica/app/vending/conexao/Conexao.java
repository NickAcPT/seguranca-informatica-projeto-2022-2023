package pt.ua.segurancainformatica.app.vending.conexao;

import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class Conexao {
    private Conexao() {
        throw new IllegalStateException("Utility class");
    }

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
        if (null == allProdutos) {
            buscarProdutos();
            return new ArrayList<>();
        }
        return allProdutos.values().stream().filter(produto -> produto.getGenero().equals(genero))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void inserirPedido(Pedido pedido) {
        System.out.println(pedido);
    }

    public static void guardaPedido(@Nullable String contribuinte) {

        double total = Entrypoint.getProdutosLista().stream()
                .mapToDouble(p -> p.getPreco() * p.getQuantidade())
                .sum();

        Pedido pedido = new Pedido(UUID.randomUUID(), contribuinte, total);

        for (ElementoComQuantidade elementoComQuantidade : Entrypoint.getProdutosLista()) {
            if (elementoComQuantidade instanceof MenuComQuantidade mcq) {
                pedido.getMenus().add(mcq);
            } else if (elementoComQuantidade instanceof ProdutoComQuantidade pcq) {
                pedido.getProdutos().add(pcq);
            }
        }

        inserirPedido(pedido);
    }
}
