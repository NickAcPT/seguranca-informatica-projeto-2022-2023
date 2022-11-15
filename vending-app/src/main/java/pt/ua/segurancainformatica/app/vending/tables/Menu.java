package pt.ua.segurancainformatica.app.vending.tables;

import java.util.List;
import java.util.Objects;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

public class Menu {
    private @Nullable Image fotoImage;
    private final int id_menu;
    private final String nome;
    private final double preco;
    private final String foto;
    private final String detalhes;
    private final String genero;
    private final List<Produto> produtos;
    public Menu(int id_menu, String nome, double preco, String foto, String detalhes, String genero, List<Produto> produtos) {
        this.id_menu = id_menu;
        this.nome = nome;
        this.preco = preco;
        this.foto = foto;
        this.detalhes = detalhes;
        this.genero = genero;
        this.produtos = produtos;

        if (null != foto && !"".equals(foto)) {
            this.fotoImage = new Image(foto, true);
        }
    }

    @Override
    public String toString() {
        return "Menu{" +
                "fotoImage=" + fotoImage +
                ", id_menu=" + id_menu +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", foto='" + foto + '\'' +
                ", detalhes='" + detalhes + '\'' +
                ", genero='" + genero + '\'' +
                ", produtos=" + produtos +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public @Nullable Image getFotoImage() {
        return fotoImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof final Menu menu)) return false;
        return id_menu == menu.id_menu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_menu);
    }
}
