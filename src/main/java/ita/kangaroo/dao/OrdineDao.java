package ita.kangaroo.dao;

import ita.kangaroo.model.OrderProductBean;
import ita.kangaroo.model.OrdineBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineDao {

    private static final Logger LOGGER = Logger.getLogger(OrdineDao.class.getName() );
    private static final String TABLE_NAME = "ordine";


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

    public OrdineDao(){
        //costruttore vuoto
    }
    public synchronized void doSave(OrdineBean order) throws SQLException {
        //SALVA NEL DATABASE UN'ISTANZA DELLA TABELLA ORDINE E DELLA RELAZIONE COMPOSIZIONE

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ComposizioneDao model = new ComposizioneDao();


        String insertSQL = "INSERT INTO " + TABLE_NAME +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setString(2, order.getClient().getUsername());
            preparedStatement.setFloat(3, order.getPrezzo_totale());
            preparedStatement.setString(4, order.getDestinatario());
            preparedStatement.setString(5,order.getMetodo_di_pagamento());
            preparedStatement.setString(6, order.getCircuito());
            preparedStatement.setString(7, order.getNumero_carta());
            preparedStatement.setString(8, order.getIndirizzo_di_spedizione());
            preparedStatement.setString(9, order.getNumero_di_tracking());
            preparedStatement.setDate(10, order.getData());
            preparedStatement.setString(11, order.getMetodo_di_spedizione());

            preparedStatement.executeUpdate();


            for (OrderProductBean prodotto : (ArrayList<OrderProductBean>) order.getProducts()){
                model.doSave(prodotto);
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


    }

    public synchronized OrdineBean doRetrieveByKey(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ComposizioneDao ordermodel = new ComposizioneDao();
        ArrayList<OrderProductBean> products = ordermodel.doRetrieveByKey(id);

        utenteDao userModel = new utenteDao();

        OrdineBean bean = new OrdineBean(products);

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " where Id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bean.setId(rs.getInt("Id"));
                bean.setClient(userModel.doRetrieveByKey(rs.getString("Username")) );
                bean.setPrezzo_totale(rs.getFloat("PrezzoTotale"));
                bean.setDestinatario(rs.getString("destinatario"));
                bean.setMetodo_di_pagamento(rs.getString("metodo_di_pagamento"));
                bean.setCircuito(rs.getString("circuito"));
                bean.setNumero_carta(rs.getString("numero_carta"));
                bean.setIndirizzo_di_spedizione(rs.getString("indirizzo_di_spedizione"));
                bean.setNumero_di_tracking(rs.getString("numero_di_tracking"));
                bean.setData(rs.getDate("data"));
                bean.setMetodo_di_spedizione(rs.getString("metodo_di_spedizione"));
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


        return bean;
    }

    public synchronized OrdineBean lastOrder() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        utenteDao userModel = new utenteDao();

        OrdineBean bean = new OrdineBean();

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " ORDER BY Id DESC LIMIT 1";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bean.setId(rs.getInt("Id"));
                bean.setClient(userModel.doRetrieveByKey(rs.getString("Username")) );
                bean.setPrezzo_totale(rs.getFloat("PrezzoTotale"));
                bean.setDestinatario(rs.getString("destinatario"));
                bean.setMetodo_di_pagamento(rs.getString("metodo_di_pagamento"));
                bean.setCircuito(rs.getString("circuito"));
                bean.setNumero_carta(rs.getString("numero_carta"));
                bean.setIndirizzo_di_spedizione(rs.getString("indirizzo_di_spedizione"));
                bean.setNumero_di_tracking(rs.getString("numero_di_tracking"));
                bean.setData(rs.getDate("data"));
                bean.setMetodo_di_spedizione(rs.getString("metodo_di_spedizione"));
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
        return bean;
    }

    public synchronized ArrayList<OrdineBean> doRetrieveByClient(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        utenteDao userModel = new utenteDao();


        String selectSQL = "SELECT * FROM " + TABLE_NAME + " where Username = ? ORDER BY Id DESC";

        ArrayList<OrdineBean> orders = new ArrayList<OrdineBean>();

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean bean = new OrdineBean();

                bean.setId(rs.getInt("Id"));
                bean.setClient(userModel.doRetrieveByKey(rs.getString("Username")) );
                bean.setPrezzo_totale(rs.getFloat("PrezzoTotale"));
                bean.setDestinatario(rs.getString("destinatario"));
                bean.setMetodo_di_pagamento(rs.getString("metodo_di_pagamento"));
                bean.setCircuito(rs.getString("circuito"));
                bean.setNumero_carta(rs.getString("numero_carta"));
                bean.setIndirizzo_di_spedizione(rs.getString("indirizzo_di_spedizione"));
                bean.setNumero_di_tracking(rs.getString("numero_di_tracking"));
                bean.setData(rs.getDate("data"));
                bean.setMetodo_di_spedizione(rs.getString("metodo_di_spedizione"));

                orders.add(bean);
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
        return orders;
    }

    public synchronized ArrayList<OrdineBean> ClientDateOrders(String username, String data_da, String data_a) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        utenteDao userModel = new utenteDao();


        String selectSQL = "SELECT * FROM " + TABLE_NAME + " where Username = ? and data >= ? and data <= ? ORDER BY Id DESC";

        ArrayList<OrdineBean> orders = new ArrayList<OrdineBean>();

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, data_da);
            preparedStatement.setString(3, data_a);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean bean = new OrdineBean();

                bean.setId(rs.getInt("Id"));
                bean.setClient(userModel.doRetrieveByKey(rs.getString("Username")) );
                bean.setPrezzo_totale(rs.getFloat("PrezzoTotale"));
                bean.setDestinatario(rs.getString("destinatario"));
                bean.setMetodo_di_pagamento(rs.getString("metodo_di_pagamento"));
                bean.setCircuito(rs.getString("circuito"));
                bean.setNumero_carta(rs.getString("numero_carta"));
                bean.setIndirizzo_di_spedizione(rs.getString("indirizzo_di_spedizione"));
                bean.setNumero_di_tracking(rs.getString("numero_di_tracking"));
                bean.setData(rs.getDate("data"));
                bean.setMetodo_di_spedizione(rs.getString("metodo_di_spedizione"));

                orders.add(bean);
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

        return orders;
    }

    public synchronized ArrayList<OrdineBean> DateOrders(String data_da, String data_a) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        utenteDao userModel = new utenteDao();


        String selectSQL = "SELECT * FROM " + TABLE_NAME + " where data >= ? and data <= ? ORDER BY id DESC";

        ArrayList<OrdineBean> orders = new ArrayList<OrdineBean>();

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, data_da);
            preparedStatement.setString(2, data_a);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean bean = new OrdineBean();

                bean.setId(rs.getInt("Id"));
                bean.setClient(userModel.doRetrieveByKey(rs.getString("Username")) );
                bean.setPrezzo_totale(rs.getFloat("PrezzoTotale"));
                bean.setDestinatario(rs.getString("destinatario"));
                bean.setMetodo_di_pagamento(rs.getString("metodo_di_pagamento"));
                bean.setCircuito(rs.getString("circuito"));
                bean.setNumero_carta(rs.getString("numero_carta"));
                bean.setIndirizzo_di_spedizione(rs.getString("indirizzo_di_spedizione"));
                bean.setNumero_di_tracking(rs.getString("numero_di_tracking"));
                bean.setData(rs.getDate("data"));
                bean.setMetodo_di_spedizione(rs.getString("metodo_di_spedizione"));

                orders.add(bean);
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


        return orders;
    }



    public synchronized ArrayList < OrdineBean > doRetrieveAll() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ArrayList < OrdineBean > orders = new ArrayList < OrdineBean > ();

        utenteDao userModel = new utenteDao();

        String selectSQL = "SELECT * FROM " + TABLE_NAME;


        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean bean = new OrdineBean();

                bean.setId(rs.getInt("Id"));
                bean.setClient(userModel.doRetrieveByKey(rs.getString("Username")) );
                bean.setPrezzo_totale(rs.getFloat("PrezzoTotale"));
                bean.setDestinatario(rs.getString("destinatario"));
                bean.setMetodo_di_pagamento(rs.getString("metodo_di_pagamento"));
                bean.setCircuito(rs.getString("circuito"));
                bean.setNumero_carta(rs.getString("numero_carta"));
                bean.setIndirizzo_di_spedizione(rs.getString("indirizzo_di_spedizione"));
                bean.setNumero_di_tracking(rs.getString("numero_di_tracking"));
                bean.setData(rs.getDate("data"));
                bean.setMetodo_di_spedizione(rs.getString("metodo_di_spedizione"));


                orders.add(bean);
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
        return orders;
    }
}
