package ptda.projeto.demo.conexao;

import ptda.projeto.demo.Tabelas.MenuComQuantidade;
import ptda.projeto.demo.Tabelas.ProdutoComQuantidade;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Pedido {

    private UUID idFatura;
    private ArrayList<ProdutoComQuantidade> produtos;
    private ArrayList<MenuComQuantidade> menus;
    private Instant data;
    private String contribuinte;
    private double preco;

    public Pedido(UUID idFatura, String contribuinte, double preco) {
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

    public String getContribuinte() {
        return contribuinte;
    }

    public double getPreco() {
        return preco;
    }

}
