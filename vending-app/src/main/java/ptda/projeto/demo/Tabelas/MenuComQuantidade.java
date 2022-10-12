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

    public Menu getMenu() {
        return menu;
    }

    @Override
    public String getNome() {
        return menu.getNome();
    }

    @Override
    public double getPreco() {
        return menu.getPreco();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuComQuantidade that = (MenuComQuantidade) o;
        return menu.equals(that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), menu);
    }
}
