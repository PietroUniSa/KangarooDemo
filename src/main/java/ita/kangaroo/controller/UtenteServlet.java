package ita.kangaroo.controller;


import ita.kangaroo.dao.AddressDao;
import ita.kangaroo.dao.MetodoPagamentoDao;
import ita.kangaroo.dao.utenteDao;
import ita.kangaroo.model.AddressBean;
import ita.kangaroo.model.MetodoPagamentoBean;
import ita.kangaroo.model.utenteBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/UtenteServlet")
public class UtenteServlet extends HttpServlet {

    static final Logger LOGGER = Logger.getLogger(DettagliS.class.getName() );
    static utenteDao clientModel = new utenteDao();
    static AddressDao addressmodel = new AddressDao();
    static MetodoPagamentoDao paymentmodel = new MetodoPagamentoDao();

    public UtenteServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        utenteBean utente = (utenteBean) request.getSession().getAttribute("utente");
        if (utente == null){
            response.sendRedirect("LoginServlet");
            return;
        }
        if (utente.getEmail().equals("provoloni@example.com")){ //se l'utente è l'admin, viene reindirizzato alla pagine di visualizzazioni dei clienti tramite la servlet admin
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AdminServlet?action=clientsNoFilter");
            dispatcher.forward(request, response);
            return;
        }


        ArrayList<AddressBean> addresses = null;
        try {
            addresses = addressmodel.doRetrieveByClient(utente.getUsername());
        } catch (SQLException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );
            response.sendRedirect("./ErrorPage/generalError.jsp");
            return;
        }

        ArrayList<MetodoPagamentoBean> payments = null;
        try {
            payments = paymentmodel.doRetrieveByClient(utente.getUsername());
        }  catch (SQLException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );
            response.sendRedirect("./ErrorPage/generalError.jsp");
            return;
        }

        request.setAttribute("addresses", addresses);
        request.setAttribute("payments", payments);


        if(action==null || action.equals("")) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cliente.jsp");
            dispatcher.forward(request, response);
            return;
        }


        if(action != null){

            if(action.equalsIgnoreCase("modify")){ // modifica dei dati personali con controllo dei form server side

                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String indirizzo = request.getParameter("indirizzo");
                String citta = request.getParameter("citta");
                String provincia = request.getParameter("provincia");
                String cap = request.getParameter("cap");
                String telefono = request.getParameter("telefono");
                String email = request.getParameter("email");


                if(nome==null || !nome.matches("^[A-Za-z ]+$")){
                    sendError(request, response);
                    return;
                }
                if(cognome==null || !cognome.matches("^[A-Za-z ]+$")){
                    sendError(request, response);
                    return;
                }
                if(indirizzo==null || !indirizzo.matches("^([A-Za-z]+\\s)+\\d+$")){
                    sendError(request, response);
                    return;
                }
                if(citta==null || !citta.matches("^[A-Za-z ]+$")){
                    sendError(request, response);
                    return;
                }
                if(provincia==null || !provincia.matches("^[A-Za-z ]+$")){
                    sendError(request, response);
                    return;
                }
                if(cap==null || !cap.matches("^\\d{5}$")){
                    sendError(request, response);
                    return;
                }
                if(telefono==null || !telefono.matches("^\\d{12}$")){
                    sendError(request, response);
                    return;
                }
                if(email==null || !email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")){
                    sendError(request, response);
                    return;
                }

                utente.setNome(nome);
                utente.setCognome(cognome);
                utente.setVia(indirizzo);
                utente.setCitta(citta);
                utente.setProvincia(provincia);
                utente.setCap(cap);
                utente.setTelefono(telefono);
                utente.setEmail(email);

                try {
                    clientModel.doModify(utente);
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
            }

            if(action.equalsIgnoreCase("addPaymentCard")){ // aggiunta di un metodo di pagamento con controlli server side
                MetodoPagamentoBean card = new MetodoPagamentoBean();
                int id = -1;

                String numcarta = request.getParameter("numero_carta");
                String cvv =  request.getParameter("cvv");
                String datascadenza = request.getParameter("data_scadenza");
                String circuito = request.getParameter("circuito");

                if(numcarta==null || !numcarta.matches("^\\d{13,19}$")){
                    sendError(request, response);
                    return;
                }
                if(cvv==null || !cvv.matches("^\\d{3}$")){
                    sendError(request, response);
                    return;
                }
                if(datascadenza==null || !datascadenza.matches("^\\d{4}\\-\\d{2}\\-\\d{2}$")){
                    sendError(request, response);
                    return;
                }
                if(circuito==null || !circuito.matches("^[A-Za-z ]+$")){
                    sendError(request, response);
                    return;
                }

                card.setNumero_carta(numcarta);
                card.setCvv(cvv);
                card.setData_scadenza(datascadenza);
                card.setCircuito(circuito);
                card.setUsername(utente.getUsername());

                try {
                    id = paymentmodel.doSave(card);
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
                card.setId(id);
                payments.add(card);
                request.setAttribute("payments", payments);
            }

            if(action.equalsIgnoreCase("deletePaymentCard")){ // eliminazione di un metodo di pagamento
                int idcarta = Integer.parseInt(request.getParameter("id_carta"));

                MetodoPagamentoBean bean = null;

                try {
                    bean = paymentmodel.doRetrieveByKey(idcarta);
                }catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
                payments.remove(bean);
                request.setAttribute("payments", payments);

                try {
                    paymentmodel.doDelete(idcarta);
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                }
            }

            if(action.equalsIgnoreCase("addAddress")){ // aggiunta di un indirizzo con controlli form server side
                AddressBean address = new AddressBean();
                int id = -1;

                String via_indirizzo = request.getParameter("via_indirizzo");
                String citta_indirizzo = request.getParameter("citta_indirizzo");
                String CAP = request.getParameter("CAP_indirizzo");

                if(citta_indirizzo==null || !citta_indirizzo.matches("^[A-Za-z ]+$")){
                    sendError(request, response);
                    return;
                }
                if(via_indirizzo==null || !via_indirizzo.matches("^([A-Za-z]+\\s)+\\d+$")){
                    sendError(request, response);
                    return;
                }
                if(CAP==null || !CAP.matches("^\\d{5}$")){
                    sendError(request, response);
                    return;
                }

                address.setVia(via_indirizzo);
                address.setCitta(citta_indirizzo);
                address.setCAP(CAP);
                address.setUsername(utente.getUsername());

                try {
                    id = addressmodel.doSave(address);
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
                address.setId(id);
                addresses.add(address);
                request.setAttribute("addresses", addresses);
            }

            if(action.equalsIgnoreCase("deleteAddress")){ // eliminazione di un indirizzo
                int idindirizzo = Integer.parseInt(request.getParameter("id_indirizzo"));
                AddressBean bean = null;

                try {
                    bean = addressmodel.doRetrieveByKey(idindirizzo);
                }catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
                addresses.remove(bean);
                request.setAttribute("addresses", addresses);

                try {
                    addressmodel.doDelete(Integer.parseInt(request.getParameter("id_indirizzo")));
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

            }
        }


        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cliente.jsp");
        dispatcher.forward(request, response);


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }

    public void sendError(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        request.setAttribute("error", "KangarooBuy ha riscontrato un problema. Perfavore, compila il form in maniera corretta e controlla i tuoi dati prima di inviarli.");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cliente.jsp");
        dispatcher.forward(request, response);
    }
}