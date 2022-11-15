package pt.ua.segurancainformatica.app.vending.tables;

import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

public class Produto {

    private final int idProduto;
    private final String nome;
    private final String genero;
    private final double preco;
    private @Nullable Image fotoImage;

    public Produto(int idProduto, String nome, String genero, String foto, double preco) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.genero = genero;
        this.preco = preco;
        if (null != foto && !"".equals(foto)) {
            this.fotoImage = new Image(foto, true);
        }
    }

    public int getIdProduto() {
        return idProduto;
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
