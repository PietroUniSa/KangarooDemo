package ita.kangaroo.controller;

import java.io.IOException;

import java.sql.SQLException;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;


import ita.kangaroo.dao.OrdineDao;

import ita.kangaroo.model.OrdineBean;
import ita.kangaroo.model.utenteBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/OrdiniUtenteServlet")
public class OrdiniUtenteServlet extends HttpServlet{

    static OrdineDao orderModel = new OrdineDao();
    private static final Logger LOGGER = Logger.getLogger( GestioneCart.class.getName() );

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        ArrayList<OrdineBean> orders = new ArrayList<OrdineBean>();
        utenteBean client = (utenteBean) request.getSession().getAttribute("utente");
        if (client == null){
            response.sendRedirect("LoginServlet");
            return;
        }

        else if (!client.getEmail().equals("provoloni@example.com")){
            //se non è admin allora è un utente generico
            String username = client.getUsername();
            try {
                orders = orderModel.doRetrieveByClient(username);
            } catch (SQLException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("./ErrorPage/generalError.jsp");
                return;
            }

        }else if (client.getEmail().equals("provoloni@example.com")){
            //se è un admin
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AdminServlet?action=ordersNoFilter"); //fa il dispatch alla servlet che gestisce tutto ciò che riguarda le operazioni da admin
            dispatcher.forward(request, response);
            return;
        }

        request.setAttribute("ordini", orders);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientsorder.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}