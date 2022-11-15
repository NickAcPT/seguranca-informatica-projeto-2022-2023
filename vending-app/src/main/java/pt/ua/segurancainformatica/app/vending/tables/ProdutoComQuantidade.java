package pt.ua.segurancainformatica.app.vending.tables;

import java.util.Objects;

public class ProdutoComQuantidade extends ElementoComQuantidade {
    private final Produto produto;

    public ProdutoComQuantidade(final Produto produto, final int quantidade) {
        super(quantidade);
        this.produto = produto;
    }

    @Override
    public double getPreco() {
        return Objects.requireNonNull(this.produto).getPreco();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoComQuantidade that)) return false;
        return Objects.equals(this.produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.produto);
    }

    public Produto getProduto() {
        return this.produto;
    }
}
