package ptda.projeto.demo.tables;

import java.util.List;
import java.util.Objects;
import javafx.scene.image.Image;

public class Menu {
    private Image fotoImage;
    private final int id_menu;
    private final String nome;
    private final double preco;
    private final String foto;
    private final String detalhes;
    private final String genero;
    private final List<Produto> produtos;
    public Menu(final int id_menu, final String nome, final double preco, final String foto, final String detalhes, final String genero, final List<Produto> produtos) {
        this.id_menu = id_menu;
        this.nome = nome;
        this.preco = preco;
        this.foto = foto;
        this.detalhes = detalhes;
        this.genero = genero;
        this.produtos = produtos;

        if (null != foto && !"".equals(foto)) {
            fotoImage = new Image(foto, true);
        }
    }

    @Override
    public String toString() {
        return "Menu{" +
                "fotoImage=" + this.fotoImage +
                ", id_menu=" + this.id_menu +
                ", nome='" + this.nome + '\'' +
                ", preco=" + this.preco +
                ", foto='" + this.foto + '\'' +
                ", detalhes='" + this.detalhes + '\'' +
                ", genero='" + this.genero + '\'' +
                ", produtos=" + this.produtos +
                '}';
    }

    public String getNome() {
        return this.nome;
    }

    public double getPreco() {
        return this.preco;
    }

    public Image getFotoImage() {
        return this.fotoImage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final Menu menu = (Menu) o;
        return this.id_menu == menu.id_menu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id_menu);
    }
}
