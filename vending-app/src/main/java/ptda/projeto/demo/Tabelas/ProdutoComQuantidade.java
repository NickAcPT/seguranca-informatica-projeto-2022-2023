package ptda.projeto.demo.Tabelas;

import java.util.Objects;

public class ProdutoComQuantidade {
    private Produto produto;

    private int quantidade;
    protected ProdutoComQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public ProdutoComQuantidade(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return produto.getNome();
    }

    public double getPreco() {
        return produto.getPreco();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoComQuantidade that = (ProdutoComQuantidade) o;
        return produto.equals(that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produto);
    }

    public Produto getProduto() {
        return produto;
    }
}
