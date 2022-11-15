package ptda.projeto.demo.tables;

import java.util.Objects;

public class ProdutoComQuantidade {
    private Produto produto;

    private int quantidade;

    protected ProdutoComQuantidade(final int quantidade) {
        this.quantidade = quantidade;
    }

    public ProdutoComQuantidade(final Produto produto, final int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(final int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return this.produto.getPreco();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoComQuantidade that)) return false;
        return this.produto.equals(that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.produto);
    }

    public Produto getProduto() {
        return this.produto;
    }
}
