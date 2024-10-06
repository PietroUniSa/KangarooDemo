package ita.kangaroo.controller;

import ita.kangaroo.dao.ComposizioneDao;
import ita.kangaroo.dao.FatturazioneDao;
import ita.kangaroo.dao.OrdineDao;
import ita.kangaroo.dao.prodottoDao;
import ita.kangaroo.model.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DettagliOrdini")
public class DettagliOrdini extends HttpServlet {

    static OrdineDao orderModel = new OrdineDao();
    static ComposizioneDao orderProdModel = new ComposizioneDao();
    static prodottoDao prodModel = new prodottoDao();
    static FatturazioneDao invoiceModel = new FatturazioneDao();

    private static final Logger LOGGER = Logger.getLogger(GestioneCart.class.getName() );

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        utenteBean client = (utenteBean) request.getSession().getAttribute("utente");
        if (client == null){
            response.sendRedirect("LoginServlet");
            return;
        }
        String action = request.getParameter("action");

        //se l'azione è null, viene preso dalla request l'id dell'ordine cercato per vederne i dettagli
        if(action==null){
            int id = Integer.parseInt(request.getParameter("ordine"));

            OrdineBean order = new OrdineBean();

            try {
                order = orderModel.doRetrieveByKey(id);
            } catch (SQLException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("./ErrorPage/generalError.jsp");
                return;
            }
            //se si è loggati oppure si è l'admin, l'ordine viene settato coem attributo nella request
            if(order.getClient().getUsername().equalsIgnoreCase(client.getUsername()) || client.getEmail().equals("provoloni@example.com")){
                request.setAttribute("detailedOrder", order);
            }else{ //utente non loggato e non admin che tenta di accedere ai dettaglio di un ordine
                response.sendRedirect("HomeServlet");
            }
        }//azione per mostrare la fattura
        if(action!=null && action.equalsIgnoreCase("viewInvoice")){

            int idordine = Integer.parseInt(request.getParameter("idOrder"));
            FatturazioneBean invoice = null;

            ArrayList<OrderProductBean> products = null; // dati necessari alla creazione della fattura
            try {
                products = orderProdModel.doRetrieveByKey(idordine);
            } catch (SQLException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("./ErrorPage/generalError.jsp");
                return;
            }

            ArrayList<ProdottoBean> product = new ArrayList<ProdottoBean>(); // dati necessari alla creazione della fattura
            for(OrderProductBean prodotto : products){
                ProdottoBean prod = null;
                try {
                    prod = prodModel.doRetrieveByKey(prodotto.getId_prodotto());
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
                product.add(prod);
            }

            try {
                invoice = invoiceModel.doRetrieveByOrder(idordine); // dati necessari alla creazione della fattura
            } catch (SQLException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("generalError.jsp");
                return;
            }

            request.setAttribute("products", product);
            request.setAttribute("orderProducts", products);
            request.setAttribute("invoice", invoice);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/fatturazione.jsp");
            dispatcher.forward(request, response);
            return;
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/orderDetails.jsp");
        dispatcher.forward(request, response);
        return;

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }
}