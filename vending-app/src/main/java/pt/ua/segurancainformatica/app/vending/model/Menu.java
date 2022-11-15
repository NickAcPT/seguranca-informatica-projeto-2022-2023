package pt.ua.segurancainformatica.app.vending.model;

import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class Menu {
    private final int idMenu;
    private final String nome;
    private final double preco;
    private final String foto;
    private final String detalhes;
    private final String genero;
    private final List<Produto> produtos;
    private @Nullable Image fotoImage;

    public Menu(int idMenu, String nome, double preco, String foto, String detalhes, String genero, List<Produto> produtos) {
        this.idMenu = idMenu;
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
                ", id_menu=" + idMenu +
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
        return idMenu == menu.idMenu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMenu);
    }
}
