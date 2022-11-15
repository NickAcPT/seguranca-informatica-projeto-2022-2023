package pt.ua.segurancainformatica.app.vending.conexao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.app.vending.Entrypoint;
import pt.ua.segurancainformatica.app.vending.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Conexao {
    private static final List<Produto> produtos = new ArrayList<>();
    private static final List<Menu> menus = new ArrayList<>();

    private Conexao() {
        throw new IllegalStateException("Utility class");
    }

    private static <T> List<T> loadFromCsv(Function<CSVRecord, T> creator, String filename) {
        var localValues = new ArrayList<T>();
        try (InputStream inputStream = Conexao.class.getResourceAsStream(filename)) {
            if (inputStream == null) throw new IOException("File not found");

            var lines = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            for (CSVRecord record : CSVFormat.DEFAULT.builder().setHeader().build().parse(new StringReader(lines))) {
                localValues.add(creator.apply(record));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return localValues;
    }

    public static void buscarProdutos() {
        if (!produtos.isEmpty()) return;

        produtos.addAll(loadFromCsv((record) -> new Produto(Integer.parseInt(record.get("id_produto")),
                record.get("nome"),
                record.get("genero"),
                record.get("foto"),
                Double.parseDouble(record.get("preco"))), "/produto.csv"));
    }

    public static void buscarMenus() {
        if (!menus.isEmpty()) return;

        menus.addAll(loadFromCsv((record) -> new Menu(Integer.parseInt(record.get("id_menu")),
                record.get("nome"),
                Double.parseDouble(record.get("preco")),
                record.get("foto"),
                record.get("detalhes"),
                record.get("genero"),
                new ArrayList<>()), "/menu.csv"));

        loadFromCsv((record) -> {
            var productId = Integer.parseInt(record.get("id_produto"));
            var menuId = Integer.parseInt(record.get("id_menu"));

            menus.stream()
                    .filter(it -> it.idMenu() == menuId).findFirst()
                    .ifPresent(menu -> menu.produtos().add(produtos.stream()
                            .filter(it -> it.getIdProduto() == productId).findFirst().orElse(null))
                    );
            return null;
        }, "/menuproduto.csv");
    }

    public static Collection<Menu> getMenus() {
        return menus;
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
        return produtos.stream().filter(produto -> produto.getGenero().equals(genero)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static void inserirPedido(Pedido pedido) {
        System.out.println(pedido);
    }

    public static void guardaPedido(@Nullable String contribuinte) {
        double total = Entrypoint.getProdutosLista().stream().mapToDouble(p -> p.getPreco() * p.getQuantidade()).sum();

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
