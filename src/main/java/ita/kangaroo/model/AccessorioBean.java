package ita.kangaroo.model;

public class AccessorioBean {
        private int id;
        private String colore;
        private String taglia;
        private ProdottoBean ProdottoId;

    public AccessorioBean(int id, String colore, String taglia, ProdottoBean prodottoId) {
        this.id = id;
        this.colore = colore;
        this.taglia = taglia;
        ProdottoId = prodottoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }

    public int getProdottoId() {
        return ProdottoId.getId();
    }

    public void setProdottoId(ProdottoBean prodottoId) {
        ProdottoId = prodottoId;
    }
}
