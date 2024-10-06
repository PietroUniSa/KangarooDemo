package ita.kangaroo.controller;

import java.io.IOException;

import java.sql.SQLException;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import ita.kangaroo.dao.AddressDao;
import ita.kangaroo.dao.MetodoPagamentoDao;
import ita.kangaroo.dao.prodottoDao;
import ita.kangaroo.model.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GestioneCart")
public class GestioneCart extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger( GestioneCart.class.getName() );

    private static prodottoDao model = new prodottoDao();
    private static AddressDao addressModel = new AddressDao();
    private static MetodoPagamentoDao paymentModel = new MetodoPagamentoDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cart cart = (Cart)request.getSession().getAttribute("cart");
        if(cart == null) {
            cart = new Cart();
            request.getSession().setAttribute("cart", cart);
        }

        utenteBean client = (utenteBean) request.getSession().getAttribute("utente");

        if (client!= null && client.getEmail().equals("provoloni@example.com")) {

            response.sendRedirect("HomeServlet");
            return;

        }

        String id = request.getParameter("id");

        String action = request.getParameter("action");
        if(action == null)
            action = "seeCart";

        ProdottoBean prod = new ProdottoBean();

        try {
            prod = model.doRetrieveByKey(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );

        } catch (SQLException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );
            response.sendRedirect("./ErrorPage/generalError.jsp");
            return;
        }


        if (action.equals("add")){
            //controllo che ci siano abbastanza prodotti da aggiungere al carrello
            if(prod.getQuantita()>0)
                cart.addProduct(prod);
            else {

                request.setAttribute("erroresoldout", "Siamo spiacenti ma questo prodotto e' esaurito");
            }

        }

        if (action.equals("Elimina dal carrello"))
            cart.removeProduct(prod);

        if (action.equals("Modifica Quantita")){
            if((prod.getQuantita() - Integer.parseInt(request.getParameter("quantity"))) >= 0 )
                cart.changeQuantity(prod, Integer.parseInt(request.getParameter("quantity")));
            else {

                request.setAttribute("erroredisponibilita", "Hai selezionato troppi di questi prodotti! Prova con una quantita minore.");

            }


        }

        if (cart != null && cart.getProducts().size() != 0){ // "procedi al pagamento" : se l'utente non è loggato, lo porta alla login.jsp
            if(action.equalsIgnoreCase("buy")) {
                // se non è loggato lo portiamo al login
                if(client == null) {
                    response.sendRedirect("LoginServlet");
                    return;
                }
                request.getSession().setAttribute("cart", cart);

                ArrayList<AddressBean> indirizzi = null;
                try {
                    indirizzi = addressModel.doRetrieveByClient(client.getUsername());
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                ArrayList<MetodoPagamentoBean> carte = null;
                try {
                    carte =  paymentModel.doRetrieveByClient(client.getUsername());
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;

                }

                if(!indirizzi.isEmpty() && !carte.isEmpty()){// "procedi al pagamento" : se l'utente è loggato ed ha almeno un metodo di pagamento e un idirizzo può procedere all'acquisto

                    request.setAttribute("addresses", indirizzi);
                    request.setAttribute("payments",carte);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GestionePagamento");
                    dispatcher.forward(request, response);
                    return;
                }
                else{ // altrimenti gli viene chiesto di inserire dei metodi di pagamento nella client.jsp
                    request.setAttribute("carterror","Perfavore aggiungi un metodo di pagamento o un indirizzo prima di procedere al pagamento.");
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UtenteServlet");
                    dispatcher.forward(request, response);
                    return;
                }

            }
        }


        request.getSession().setAttribute("cart", cart);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cart.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
