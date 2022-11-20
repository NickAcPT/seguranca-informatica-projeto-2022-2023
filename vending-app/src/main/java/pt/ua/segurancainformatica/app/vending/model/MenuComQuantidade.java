package pt.ua.segurancainformatica.app.vending.model;

import java.util.Objects;

public class MenuComQuantidade extends ElementoComQuantidade {

    private final Menu menu;

    public MenuComQuantidade(Menu menu, int quantidade) {
        super(quantidade);
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "MenuComQuantidade{" +
                "menu=" + menu +
                '}';
    }

    @Override
    public double getPreco() {
        return menu.getPreco();
    }

    @Override
    public String getNome() {
        return menu.getNome();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuComQuantidade that)) return false;
        return menu.equals(that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), menu);
    }
}
