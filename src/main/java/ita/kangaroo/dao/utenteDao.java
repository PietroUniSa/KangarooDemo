package ita.kangaroo.dao;

import ita.kangaroo.model.utenteBean;
import ita.kangaroo.model.tipo;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

public class utenteDao {

    private static final Logger LOGGER = Logger.getLogger(utenteDao.class.getName() );
    private static final String TABLE = "utente";

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

    public utenteDao(){
        //costruttore vuoto
    }

    public synchronized int doSave(utenteBean client) throws SQLException{
        //SALVA NEL DATABASE
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        String insertSQL = "INSERT INTO " + utenteDao.TABLE
                + " (Username, Password, Cognome, Nome, Email, Tipo, via, citta, provincia, telefono, cap) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, client.getUsername());
            preparedStatement.setString(2, client.getPassword());
            preparedStatement.setString(3, client.getCognome());
            preparedStatement.setString(4, client.getNome());
            preparedStatement.setString(5, client.getEmail());
            preparedStatement.setString(6, String.valueOf(client.getTipo()));
            preparedStatement.setString(7, client.getVia());
            preparedStatement.setString(8, client.getCitta());
            preparedStatement.setString(9, client.getProvincia());
            preparedStatement.setString(10, client.getTelefono());
            preparedStatement.setString(11, client.getCap());

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
        return result;
    }
    public synchronized utenteBean doRetrieveByEmailAndPassword(String email, String password) throws SQLException{
        //PRENDE UN UTENTE DAL SUO EMAIL E PASSWORD
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM " + utenteDao.TABLE + " WHERE Email=? AND Password=SHA1(?)";
        utenteBean client = null;


        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();



            while (rs.next()) {

                client = new utenteBean();

                client.setUsername(rs.getString("Username"));
                client.setPassword(rs.getString("Password"));
                client.setCognome(rs.getString("Cognome"));
                client.setNome(rs.getString("Nome"));
                client.setEmail(rs.getString("Email"));
                client.setTipo(tipo.valueOf(rs.getString("Tipo")));
                client.setVia(rs.getString("via"));
                client.setCitta(rs.getString("Citta"));
                client.setProvincia(rs.getString("provincia"));
                client.setTelefono(rs.getString("telefono"));
                client.setCap(rs.getString("cap"));
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


        return client;
    }

    public synchronized utenteBean doRetrieveByKey(String username) throws SQLException{
        //PRENDE UN UTENTE DAL SUO USERNAME
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM " + utenteDao.TABLE + " WHERE Username=?";
        utenteBean client = null;


        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();



            while (rs.next()) {

                client = new utenteBean();

                client.setUsername(rs.getString("Username"));
                client.setPassword(rs.getString("Password"));
                client.setCognome(rs.getString("Cognome"));
                client.setNome(rs.getString("Nome"));
                client.setEmail(rs.getString("Email"));
                client.setTipo(tipo.valueOf(rs.getString("Tipo")));
                client.setVia(rs.getString("via"));
                client.setCitta(rs.getString("Citta"));
                client.setProvincia(rs.getString("provincia"));
                client.setTelefono(rs.getString("telefono"));
                client.setCap(rs.getString("cap"));

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


        return client;
    }

    public synchronized int doModify(utenteBean client) throws SQLException{
        //SALVA LE MODIFICHE NEL DATABASE
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        String updateSQL = "UPDATE "+ TABLE+ " SET  Cognome = ?, Nome = ?, Email = ?, via = ?, citta = ?, provincia = ?, telefono = ?, cap = ? " + "WHERE Username = ?";


        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1, client.getCognome());
            preparedStatement.setString(2,client.getNome());
            preparedStatement.setString(3,client.getEmail());
            preparedStatement.setString(4,client.getVia());
            preparedStatement.setString(5,client.getCitta());
            preparedStatement.setString(6,client.getProvincia());
            preparedStatement.setString(7,client.getTelefono());
            preparedStatement.setString(8,client.getCap());
            preparedStatement.setString(9,client.getUsername());

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
        return result;
    }

    public synchronized utenteBean doRetrieveByEmail(String email) throws SQLException{
        //PRENDE UN UTENTE DAL SUO EMAIL E PASSWORD
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM " + utenteDao.TABLE + " WHERE Email=?";
        utenteBean client = null;


        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);


            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                client = new utenteBean();

                client.setUsername(rs.getString("Username"));
                client.setPassword(rs.getString("Password"));
                client.setCognome(rs.getString("Cognome"));
                client.setNome(rs.getString("Nome"));
                client.setEmail(rs.getString("Email"));
                client.setTipo(tipo.valueOf(rs.getString("Tipo")));
                client.setVia(rs.getString("via"));
                client.setCitta(rs.getString("Citta"));
                client.setProvincia(rs.getString("provincia"));
                client.setTelefono(rs.getString("telefono"));
                client.setCap(rs.getString("cap"));

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


        return client;
    }

    public synchronized ArrayList < utenteBean > doRetrieveAll() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ArrayList <utenteBean> clients = new ArrayList<>() ;


        String selectSQL = "SELECT * FROM " + TABLE + " WHERE Username <> 'rossif'" ;

        utenteBean client = null;

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                client = new utenteBean();

                client.setUsername(rs.getString("Username"));
                client.setPassword(rs.getString("Password"));
                client.setCognome(rs.getString("Cognome"));
                client.setNome(rs.getString("Nome"));
                client.setEmail(rs.getString("Email"));
                client.setTipo(tipo.valueOf(rs.getString("Tipo")));
                client.setVia(rs.getString("via"));
                client.setCitta(rs.getString("Citta"));
                client.setProvincia(rs.getString("provincia"));
                client.setTelefono(rs.getString("telefono"));
                client.setCap(rs.getString("cap"));

                clients.add(client);
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
        return clients;
    }
}
