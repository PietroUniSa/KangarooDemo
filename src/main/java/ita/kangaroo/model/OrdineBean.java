package ita.kangaroo.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrdineBean {

    private ArrayList<OrderProductBean> products;
    private int id;
    private utenteBean client;
    private float prezzo_totale;
    private String destinatario;
    private String metodo_di_pagamento;
    private String circuito;
    private String numero_carta;
    private String indirizzo_di_spedizione;
    private String numero_di_tracking;
    private Date data;
    private String metodo_di_spedizione;


    //costruttore per le inizializzazioni nulle
    public OrdineBean() {
        products = new ArrayList<OrderProductBean>();
        this.id = 0;
        this.client = null;
        this.prezzo_totale = 0;
        this.destinatario = null;
        this.metodo_di_pagamento = null;
        this.indirizzo_di_spedizione = null;
        this.numero_di_tracking = null;
        this.data = null;
        this.metodo_di_spedizione = null;
    }

    //costruttore con prodotti pronti
    public OrdineBean(ArrayList<OrderProductBean> products) {
        setProducts(products);
        this.id = 0;
        this.client = null;
        this.prezzo_totale = 0;
        this.destinatario = null;
        this.metodo_di_pagamento = null;
        this.indirizzo_di_spedizione = null;
        this.numero_di_tracking = null;
        this.data = null;
        this.metodo_di_spedizione = null;
    }

    //costruttore con cliente pronto
    public OrdineBean(utenteBean client) {
        products = new ArrayList<OrderProductBean>();
        this.id = 0;
        setClient(client);
        this.prezzo_totale = 0;
        this.destinatario = null;
        this.metodo_di_pagamento = null;
        this.indirizzo_di_spedizione = null;
        this.numero_di_tracking = null;
        this.data = null;
        this.metodo_di_spedizione = null;
    }

    public OrdineBean(int id, utenteBean client, float prezzo_totale, String destinatario,
                      String metodo_di_pagamento, String circuito, String numero_carta,
                      String indirizzo_di_spedizione, String numero_di_tracking, Date data,
                      String metodo_di_spedizione) {
        this.id = id;
        this.client = client;
        this.prezzo_totale = prezzo_totale;
        this.destinatario = destinatario;
        this.metodo_di_pagamento = metodo_di_pagamento;
        this.circuito = circuito;
        this.numero_carta = numero_carta;
        this.indirizzo_di_spedizione = indirizzo_di_spedizione;
        this.numero_di_tracking = numero_di_tracking;
        this.data = data;
        this.metodo_di_spedizione = metodo_di_spedizione;
        this.products = new ArrayList<OrderProductBean>(); // Inizializzazione dell'ArrayList
    }

    public ArrayList<OrderProductBean> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<OrderProductBean> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public utenteBean getClient() {
        return client;
    }

    public void setClient(utenteBean client) {
        this.client = client;
    }

    public float getPrezzo_totale() {
        return prezzo_totale;
    }

    public void setPrezzo_totale(float prezzo_totale) {
        this.prezzo_totale = prezzo_totale;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMetodo_di_pagamento() {
        return metodo_di_pagamento;
    }

    public void setMetodo_di_pagamento(String metodo_di_pagamento) {
        this.metodo_di_pagamento = metodo_di_pagamento;
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    public String getNumero_carta() {
        return numero_carta;
    }

    public void setNumero_carta(String numero_carta) {
        this.numero_carta = numero_carta;
    }

    public String getIndirizzo_di_spedizione() {
        return indirizzo_di_spedizione;
    }

    public void setIndirizzo_di_spedizione(String indirizzo_di_spedizione) {
        this.indirizzo_di_spedizione = indirizzo_di_spedizione;
    }

    public String getNumero_di_tracking() {
        return numero_di_tracking;
    }

    public void setNumero_di_tracking(String numero_di_tracking) {
        this.numero_di_tracking = numero_di_tracking;
    }


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMetodo_di_spedizione() {
        return metodo_di_spedizione;
    }

    public void setMetodo_di_spedizione(String metodo_di_spedizione) {
        this.metodo_di_spedizione = metodo_di_spedizione;
    }

    @Override
    public String toString() {
        if (data != null) {
            LocalDate date = data.toLocalDate();
            return date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear();
        } else {
            return "Data non disponibile";
        }
    }


}