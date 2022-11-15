package pt.ua.segurancainformatica.app.vending.tables;

import java.util.Objects;

public class ProdutoComQuantidade extends ElementoComQuantidade {
    private final Produto produto;

    public ProdutoComQuantidade(Produto produto, int quantidade) {
        super(quantidade);
        this.produto = produto;
    }

    @Override
    public double getPreco() {
        return Objects.requireNonNull(produto).getPreco();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdutoComQuantidade that)) return false;
        return Objects.equals(produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produto);
    }

    public Produto getProduto() {
        return produto;
    }
}
