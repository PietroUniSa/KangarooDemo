package ita.kangaroo.model;

public class animaleBean extends ProdottoBean {
    private int id1;
    private String continente;
    private int età;
    private double peso;
    private String sesso;

    public animaleBean() {};

    public animaleBean(int id, String descrizione, String nome,String immagine, float prezzo, int quantita, float iva, tipo tipo, int id1, String continente, int età, double peso, String sesso) {
        super(id, descrizione, nome,immagine, prezzo, quantita, iva, tipo);
        this.id1 = id1;
        this.continente = continente;
        this.età = età;
        this.peso = peso;
        this.sesso = sesso;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public String getContinente() {
        return continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

    public int getEtà() {
        return età;
    }

    public void setEtà(int età) {
        this.età = età;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    @Override
    public int getId() {
        return super.getId();
    }
}
