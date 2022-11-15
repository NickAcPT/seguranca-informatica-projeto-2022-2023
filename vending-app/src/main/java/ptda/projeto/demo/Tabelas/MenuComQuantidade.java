package ptda.projeto.demo.Tabelas;

import java.util.Objects;

public class MenuComQuantidade extends ProdutoComQuantidade{

    private Menu menu;

    @Override
    public String toString() {
        return "MenuComQuantidade{" +
                "menu=" + menu +
                '}';
    }

    public MenuComQuantidade(Menu menu, int quantidade) {
        super(quantidade);
        this.menu = menu;
    }

    @Override
    public double getPreco() {
        return menu.getPreco();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuComQuantidade)) return false;
        MenuComQuantidade that = (MenuComQuantidade) o;
        return menu.equals(that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), menu);
    }
}
