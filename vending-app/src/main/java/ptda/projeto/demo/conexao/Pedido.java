package ptda.projeto.demo.conexao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import ptda.projeto.demo.tables.MenuComQuantidade;
import ptda.projeto.demo.tables.ProdutoComQuantidade;

public class Pedido {

    private final UUID idFatura;
    private final ArrayList<ProdutoComQuantidade> produtos;
    private final ArrayList<MenuComQuantidade> menus;
    private final Instant data;
    private final String contribuinte;
    private final double preco;

    public Pedido(final UUID idFatura, final String contribuinte, final double preco) {
        this.idFatura = idFatura;
        data = Instant.now();
        this.contribuinte = contribuinte;
        this.preco = preco;
        produtos = new ArrayList<>();
        menus = new ArrayList<>();
    }

    public UUID getIdFatura() {
        return this.idFatura;
    }

    public ArrayList<ProdutoComQuantidade> getProdutos() {
        return this.produtos;
    }

    public ArrayList<MenuComQuantidade> getMenus() {
        return this.menus;
    }

    public Instant getData() {
        return this.data;
    }

    public String getContribuinte() {
        return this.contribuinte;
    }

    public double getPreco() {
        return this.preco;
    }

}
