package ptda.projeto.demo.Tabelas;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Objects;

public class Menu {
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

    private Image fotoImage = null;
    private int id_menu;
    private String nome;
    private double preco;
    private String foto;
    private String detalhes;
    private String genero;
    private List<Produto> produtos;

    public Menu(int id_menu, String nome, double preco, String foto, String detalhes, String genero, List<Produto> produtos) {
        this.id_menu = id_menu;
        this.nome = nome;
        this.preco = preco;
        this.foto = foto;
        this.detalhes = detalhes;
        this.genero = genero;
        this.produtos = produtos;

        if (foto != null && !foto.equals("")) {
            this.fotoImage = new Image(foto, true);
        }
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public Image getFotoImage() {
        return fotoImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id_menu == menu.id_menu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_menu);
    }
}
