package ita.kangaroo.dao;

import ita.kangaroo.model.ProdottoBean;
import ita.kangaroo.model.tipo;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.*;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class prodottoDao {

    private static final Logger LOGGER = Logger.getLogger(prodottoDao.class.getName() );
    private static final String TABLE = "prodotto";

    private static DataSource ds;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            ds = (DataSource) envCtx.lookup("jdbc/kangaroodb");

        } catch (NamingException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );
        }
    }

    public prodottoDao(){
        //costruttore vuoto
    }

    public synchronized int doSave(ProdottoBean prod) throws SQLException{
        //SALVARE NEL DATABASE
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int id = -1;

        String insertSQL = "INSERT INTO " + TABLE
                + " (Id, Nome, Descrizione, Prezzo, QuantitaDisponibile, Tipo, IVA,immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, prod.getId());
            preparedStatement.setString(2, prod.getNome());
            preparedStatement.setString(3, prod.getDescrizione());
            preparedStatement.setDouble(4, prod.getPrezzo());
            preparedStatement.setInt(5,prod.getQuantita());
            preparedStatement.setString(6, prod.getTipo().toString());
            preparedStatement.setDouble(7, prod.getIva());
            preparedStatement.setString(8, prod.getImmagine());


            preparedStatement.executeUpdate();



            ResultSet key = preparedStatement.getGeneratedKeys();

            while(key.next()) {
                id = key.getInt(1);
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
        return id;
    }



    public synchronized ProdottoBean doRetrieveByKey(int id) throws SQLException {
        // PRENDE UN PRODOTTO DAL SUO ID
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE Id = ?";
        ProdottoBean prod = null; // Inizializza come null

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                // Crea e popola l'oggetto ProdottoBean solo se il prodotto è trovato
                prod = new ProdottoBean();
                prod.setId(rs.getInt("Id"));
                prod.setNome(rs.getString("Nome"));
                prod.setDescrizione(rs.getString("Descrizione"));
                prod.setPrezzo(rs.getFloat("Prezzo"));
                prod.setQuantita(rs.getInt("QuantitaDisponibile"));
                prod.setTipo(tipo.valueOf(rs.getString("Tipo")));
                prod.setIva(rs.getFloat("IVA"));
                prod.setImmagine(rs.getString("immagine"));
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
        return prod;
    }


    public synchronized boolean doDelete(int id) throws SQLException{
        //ELIMINA UN PRODOTTO DAL DATABASE
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;

        String deleteSQL = "DELETE FROM " + TABLE + " WHERE Id = ?";
        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, id);

            result = preparedStatement.executeUpdate();

        } finally{
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return (result != 0);
    }

    public synchronized List<ProdottoBean> doRetrieveAll() throws SQLException{
        //PRENDE TUTTI I GIOIELLI
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        List<ProdottoBean> products = new ArrayList<ProdottoBean>();

        String selectSQL = "SELECT * FROM " + TABLE ;

        try{
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                ProdottoBean prod = new ProdottoBean();

                prod.setId(rs.getInt("Id"));
                prod.setNome(rs.getString("Nome"));
                prod.setDescrizione(rs.getString("Descrizione"));
                prod.setPrezzo(rs.getFloat("Prezzo"));
                prod.setQuantita(rs.getInt("QuantitaDisponibile"));
                prod.setTipo(tipo.valueOf(rs.getString("Tipo")));
                prod.setIva(rs.getFloat("IVA"));
                prod.setImmagine(rs.getString("immagine"));

                products.add(prod);
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
        return products;
    }

    public synchronized ArrayList<ProdottoBean> doRetrieveAllLimit() throws SQLException{
        //PRENDE 10 GIOIELLI
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ArrayList<ProdottoBean> products = new ArrayList<ProdottoBean>();

        String selectSQL = "SELECT * FROM " + TABLE + " LIMIT 10";

        try{
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                ProdottoBean prod = new ProdottoBean();

                prod.setId(rs.getInt("Id"));
                prod.setNome(rs.getString("Nome"));
                prod.setDescrizione(rs.getString("Descrizione"));
                prod.setPrezzo(rs.getFloat("Prezzo"));
                prod.setQuantita(rs.getInt("QuantitaDisponibile"));
                prod.setTipo(tipo.valueOf(rs.getString("Tipo")));
                prod.setIva(rs.getFloat("IVA"));
                prod.setImmagine(rs.getString("immagine"));

                products.add(prod);
            }

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
        return products;
    }

    public synchronized boolean doModify(ProdottoBean prod) throws SQLException {
        // MODIFICA UN PRODOTTO DAL SUO ID
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;

        String updateSQL = "UPDATE " + TABLE + " SET Nome = ?, Descrizione = ?, Prezzo = ?, QuantitaDisponibile = ?, Tipo = ?, IVA = ?, immagine = ? WHERE Id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, prod.getNome());
            preparedStatement.setString(2, prod.getDescrizione());
            preparedStatement.setDouble(3, prod.getPrezzo());
            preparedStatement.setInt(4, prod.getQuantita());
            preparedStatement.setString(5, prod.getTipo().toString());
            preparedStatement.setDouble(6, prod.getIva());
            preparedStatement.setString(7, prod.getImmagine()); // Modifica qui: immagine al 7° posto
            preparedStatement.setInt(8, prod.getId()); // Modifica qui: Id all'8° posto

            result = preparedStatement.executeUpdate();

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return (result != 0);
    }

    public synchronized ArrayList<ProdottoBean> doRetrieveAllByCategory(String category) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        String selectSQL = "SELECT * FROM " + TABLE + " WHERE Tipo = ? ";

        ArrayList<ProdottoBean> beans = new ArrayList<ProdottoBean>();

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, category);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();

                prod.setId(rs.getInt("Id"));
                prod.setNome(rs.getString("Nome"));
                prod.setDescrizione(rs.getString("Descrizione"));
                prod.setPrezzo(rs.getFloat("Prezzo"));
                prod.setQuantita(rs.getInt("QuantitaDisponibile"));
                prod.setTipo(tipo.valueOf(rs.getString("Tipo")));
                prod.setIva(rs.getFloat("IVA"));
                prod.setImmagine(rs.getString("immagine"));

                beans.add(prod);
            }
        }
        finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            }
            finally {
                if (connection != null)
                    connection.close();
            }
        }
        return beans;
    }



    public synchronized void updateQuantity (int id, int newQuantity) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String updateSQL = "UPDATE " + TABLE + " SET QuantitaDisponibile = ? WHERE Id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();


        }

        finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            }

            finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

    public synchronized ArrayList<ProdottoBean> doRetrieveAllByKeyword(String keyword, String query) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE Descrizione LIKE " + "'%" + keyword + "%'" + query;

        ArrayList<ProdottoBean> beans = new ArrayList<ProdottoBean>();

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();
                prod.setId(rs.getInt("Id"));
                prod.setNome(rs.getString("Nome"));
                prod.setDescrizione(rs.getString("Descrizione"));
                prod.setPrezzo(rs.getFloat("Prezzo"));
                prod.setQuantita(rs.getInt("QuantitaDisponibile"));
                prod.setTipo(tipo.valueOf(rs.getString("Tipo")));
                prod.setIva(rs.getFloat("IVA"));
                prod.setImmagine(rs.getString("immagine"));

                beans.add(prod);
            }
        }
        finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            }
            finally {
                if (connection != null)
                    connection.close();
            }
        }
        return beans;
    }


    public synchronized ArrayList<ProdottoBean> doRetrieveAllByName(String keyword) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE Nome LIKE '" + keyword + "%'" ;

        ArrayList<ProdottoBean> beans = new ArrayList<ProdottoBean>();

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ProdottoBean prod = new ProdottoBean();
                prod.setId(rs.getInt("Id"));
                prod.setNome(rs.getString("Nome"));
                prod.setDescrizione(rs.getString("Descrizione"));
                prod.setPrezzo(rs.getFloat("Prezzo"));
                prod.setQuantita(rs.getInt("QuantitaDisponibile"));
                prod.setTipo(tipo.valueOf(rs.getString("Tipo")));
                prod.setIva(rs.getFloat("IVA"));
                prod.setImmagine(rs.getString("immagine"));

                beans.add(prod);
            }
        }
        finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            }
            finally {
                if (connection != null)
                    connection.close();
            }
        }
        return beans;
    }


}