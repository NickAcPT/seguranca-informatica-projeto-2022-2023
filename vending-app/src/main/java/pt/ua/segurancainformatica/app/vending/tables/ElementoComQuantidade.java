package pt.ua.segurancainformatica.app.vending.tables;

public abstract class ElementoComQuantidade {
    public ElementoComQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    protected int quantidade;

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(final int quantidade) {
        this.quantidade = quantidade;
    }

    public abstract double getPreco();
}
