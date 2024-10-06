package ita.kangaroo.model;

public class ciboBean extends ProdottoBean {
    private int id;
    private double peso;

    public ciboBean(int id, double peso) {
        this.id = id;
        this.peso = peso;
    }

    public ciboBean(int id, String descrizione, String nome,String immagine, float prezzo, int quantita, float iva, ita.kangaroo.model.tipo tipo, int id1, double peso) {
        super(id, descrizione, nome,immagine, prezzo, quantita, iva, tipo);
        this.id = id1;
        this.peso = peso;
    }
    @Override
    public int getId() {
        return super.getId();
    }


    @Override
    public void setId(int id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
