package pt.ua.segurancainformatica.app.vending.tables;

import java.util.Objects;

public class MenuComQuantidade extends ProdutoComQuantidade {

    private final Menu menu;

    public MenuComQuantidade(final Menu menu, final int quantidade) {
        super(quantidade);
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "MenuComQuantidade{" +
                "menu=" + this.menu +
                '}';
    }

    @Override
    public double getPreco() {
        return this.menu.getPreco();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuComQuantidade that)) return false;
        return this.menu.equals(that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.menu);
    }
}
