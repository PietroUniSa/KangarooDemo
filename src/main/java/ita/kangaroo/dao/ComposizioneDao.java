package ita.kangaroo.dao;

import ita.kangaroo.model.OrderProductBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComposizioneDao {

    private static final Logger LOGGER = Logger.getLogger(ComposizioneDao.class.getName() );
    private static final String TABLE = "composizione";

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


    public ComposizioneDao(){
        //costruttore vuoto
    }

    public synchronized void doSave(OrderProductBean product) throws SQLException{
        //SALVARE NEL DATABASE
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertSQL = "INSERT INTO " + TABLE + " VALUES (?, ?, ?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, product.getId_ordine());
            preparedStatement.setInt(2, product.getId_prodotto());
            preparedStatement.setFloat(3, product.getPrezzo());
            preparedStatement.setInt(4, product.getQuantita());
            preparedStatement.setFloat(5, product.getIVA());


            preparedStatement.executeUpdate();


        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

    public synchronized ArrayList < OrderProductBean > doRetrieveByKey(int id_ordine) throws SQLException{
        //TROVA TUTTI I COLLEGAMENTI IN "COMPOSIZIONE" IN BASE ALL'ID DELL'ORDINE
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ArrayList < OrderProductBean > products = new ArrayList < OrderProductBean > ();

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE id_ordine = ?";


        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, id_ordine);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrderProductBean product = new OrderProductBean();

                product.setId_ordine(rs.getInt("id_ordine"));
                product.setId_prodotto(rs.getInt("id_prodotto"));
                product.setPrezzo(rs.getFloat("prezzo"));
                product.setQuantita(rs.getInt("quantita"));
                product.setIVA(rs.getFloat("IVA"));

                products.add(product);


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
}
