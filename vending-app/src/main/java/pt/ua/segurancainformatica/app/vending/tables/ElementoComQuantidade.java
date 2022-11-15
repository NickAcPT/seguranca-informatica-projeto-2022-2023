package pt.ua.segurancainformatica.app.vending.tables;

public abstract class ElementoComQuantidade {
    protected ElementoComQuantidade(final int quantidade) {
        this.quantidade = quantidade;
    }

    protected int quantidade;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public abstract double getPreco();
}
