package pt.ua.segurancainformatica.app.vending.conexao;

import org.jetbrains.annotations.Nullable;
import pt.ua.segurancainformatica.app.vending.tables.MenuComQuantidade;
import pt.ua.segurancainformatica.app.vending.tables.ProdutoComQuantidade;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Pedido {

    private final UUID idFatura;
    private final ArrayList<ProdutoComQuantidade> produtos;
    private final ArrayList<MenuComQuantidade> menus;
    private final Instant data;
    private final @Nullable String contribuinte;
    private final double preco;

    public Pedido(UUID idFatura, @Nullable String contribuinte, double preco) {
        this.idFatura = idFatura;
        this.data = Instant.now();
        this.contribuinte = contribuinte;
        this.preco = preco;
        this.produtos = new ArrayList<>();
        this.menus = new ArrayList<>();
    }

    public UUID getIdFatura() {
        return idFatura;
    }

    public ArrayList<ProdutoComQuantidade> getProdutos() {
        return produtos;
    }

    public ArrayList<MenuComQuantidade> getMenus() {
        return menus;
    }

    public Instant getData() {
        return data;
    }

    public @Nullable String getContribuinte() {
        return contribuinte;
    }

    public double getPreco() {
        return preco;
    }
}
