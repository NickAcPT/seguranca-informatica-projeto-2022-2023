package ptda.projeto.demo.tables;

import javafx.scene.image.Image;

public class Produto {

    private Image fotoImage = null;
    private int id_produto;
    private String nome;
    private String genero;
    private double preco;

    public Produto(int id_produto, String nome, String genero, String foto, double preco) {
        this.id_produto = id_produto;
        this.nome = nome;
        this.genero = genero;
        this.preco = preco;
        if (foto != null && !foto.equals("")) {
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

    public Image getFotoImage() {
        return fotoImage;
    }

    public double getPreco() {
        return preco;
    }
}
