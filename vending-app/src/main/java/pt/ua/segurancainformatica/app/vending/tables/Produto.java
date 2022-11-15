package pt.ua.segurancainformatica.app.vending.tables;

import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

public class Produto {

    private @Nullable Image fotoImage;
    private final int id_produto;
    private final String nome;
    private final String genero;
    private final double preco;

    public Produto(final int id_produto, final String nome, final String genero, final String foto, final double preco) {
        this.id_produto = id_produto;
        this.nome = nome;
        this.genero = genero;
        this.preco = preco;
        if (null != foto && !"".equals(foto)) {
            fotoImage = new Image(foto, true);
        }
    }

    public int getId_produto() {
        return this.id_produto;
    }

    public String getNome() {
        return this.nome;
    }

    public String getGenero() {
        return this.genero;
    }

    public @Nullable Image getFotoImage() {
        return this.fotoImage;
    }

    public double getPreco() {
        return this.preco;
    }
}
