package ita.kangaroo.model;

public class ProdottoBean {
    private int id;
    private String descrizione;
    private String nome;
    private String immagine;
    private float prezzo;
    private int quantita;
    private float iva;
    private tipo tipo;

    public ProdottoBean() {};

    public ProdottoBean(int id, String descrizione, String nome,String immagine, float prezzo, int quantita, float iva, ita.kangaroo.model.tipo tipo) {
        this.id = id;
        this.descrizione = descrizione;
        this.nome = nome;
        this.immagine = immagine;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.iva = iva;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    public String getImmagine() {
        return immagine;
    }
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public ita.kangaroo.model.tipo getTipo() {
        return tipo;
    }


    public void setTipo(ita.kangaroo.model.tipo tipo) {
        this.tipo = tipo;
    }
    @Override
    public String toString() {
        return "prodottoBean{" +
                "id=" + id +
                ", descrizione='" + descrizione + '\'' +
                ", nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                ", quantita=" + quantita +
                ", iva=" + iva +
                ", tipo=" + tipo +
                '}';
    }
}
