package pt.ua.segurancainformatica.app.vending.tables;

import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

public class Produto {

    private final int id_produto;
    private final String nome;
    private final String genero;
    private final double preco;
    private @Nullable Image fotoImage;

    public Produto(int id_produto, String nome, String genero, String foto, double preco) {
        this.id_produto = id_produto;
        this.nome = nome;
        this.genero = genero;
        this.preco = preco;
        if (null != foto && !"".equals(foto)) {
            this.fotoImage = new Image(foto, true);
        }
    }

    public int getId_produto() {
        return id_produto;
    }

    public String getNome() {
        return nome;
    }

    public String getGenero() {
        return genero;
    }

    public @Nullable Image getFotoImage() {
        return fotoImage;
    }

    public double getPreco() {
        return preco;
    }
}
