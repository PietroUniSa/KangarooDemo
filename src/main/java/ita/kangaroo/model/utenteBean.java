package ita.kangaroo.model;

import javax.management.StringValueExp;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;


public class utenteBean {
    private String nome;
    private String cognome;
    private String email;


    private String password;
    private String username;
    private tipo tipo;

    private String via;
    private String citta;
    private String provincia;
    private String telefono;
    private String cap;

    public utenteBean() {
    }

    ;

    public utenteBean(String nome, String cognome, String email, String password, String username, ita.kangaroo.model.tipo tipo, String via, String citta, String provincia, String telefono,String cap) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;


        this.password = password;
        this.username = username;
        this.tipo = tipo;
        this.via = via;
        this.citta = citta;
        this.provincia = provincia;
        this.telefono = telefono;
        this.cap = cap;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            this.password = String.format("%040x", new
                    BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ita.kangaroo.model.tipo getTipo() {
        return tipo;
    }

    public void setTipo(ita.kangaroo.model.tipo tipo) {
        this.tipo = tipo;
    }

    public String getVia() {
        return via;
    }
    public void setVia(String via) {
        this.via = via;
    }
    public String getCitta() {
        return citta;
    }
    public void setCitta(String citta) {
        this.citta = citta;
    }
    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;

    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setCap(String cap) {
        this.cap = cap;
    }
    public String getCap() {
        return cap;
    }


}