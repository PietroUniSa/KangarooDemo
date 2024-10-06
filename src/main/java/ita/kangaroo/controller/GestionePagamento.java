package ita.kangaroo.controller;

import ita.kangaroo.dao.*;
import ita.kangaroo.model.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@WebServlet("/GestionePagamento")

public class GestionePagamento extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DettagliS.class.getName() );
    private static final long serialVersionUID = 1L;

    static OrdineDao orderModel = new OrdineDao();
    static prodottoDao prodottoModel = new prodottoDao();
    static AddressDao addressmodel = new AddressDao();
    static MetodoPagamentoDao paymentmodel = new MetodoPagamentoDao();
    static FatturazioneDao invoicemodel = new FatturazioneDao();

    public GestionePagamento() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        utenteBean utente = (utenteBean) request.getSession().getAttribute("utente");
        String card = request.getParameter("carta");
        boolean check = true;
        AddressBean bean =  null;
        FatturazioneBean invoice = new FatturazioneBean();

        if( action == null || action.equals("buy") || action.equals("")){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/checkout.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int idcarta;
        int idindirizzo;

        if(card.equals(""))  // controllo del form server side
            idcarta = 0;
        else
            idcarta = Integer.parseInt(request.getParameter("carta"));

        if(request.getParameter("indirizzo").equals(""))
            idindirizzo = 0;
        else
            idindirizzo = Integer.parseInt(request.getParameter("indirizzo"));


        String destinatario = request.getParameter("destinatario");
        String note = request.getParameter("note");
        String spedizione= request.getParameter("spedizione");
        String metpag = request.getParameter("metodo_di_pagamento");

        if(destinatario.equals("") || destinatario== null || !destinatario.matches("^[A-Za-z ]+$")){
            sendError(request, response);
            return;
        }
        if(spedizione.equals("") || spedizione== null || (!spedizione.equals("Express") && !spedizione.equals("Standard") && !spedizione.equals("Economic") )){
            sendError(request, response);
            return;
        }
        if(note.equals("") || note== null || !note.matches("^[A-Za-z ]+$")){
            sendError(request, response);
            return;
        }
        if(metpag.equals("") || metpag== null || (!metpag.equals("carta_di_credito") && !metpag.equals("carta_di_debito") && !metpag.equals("Paypal"))){
            sendError(request, response);
            return;
        }

        int id_ordine;

        OrdineBean lastorder; //necessario per settare l'id dell'ordine che si sta effettuando (bisogna fare anche le insert in composizione)
        try {
            lastorder = orderModel.lastOrder();
        } catch (SQLException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );
            response.sendRedirect("./ErrorPage/generalError.jsp");
            return;
        }

        if (lastorder== null){
            id_ordine = 0;
        }
        else{
            id_ordine = lastorder.getId();
        }


        if (cart != null && cart.getProducts().size() != 0){ // se il carrello non è vuoto, si prendono i prodotti e si salvano in composizione con chiave esterna sull'id dell'ordine

            if(action != null && action.equalsIgnoreCase("confirm_buy")) {

                //INIZIALIZZA L'ID, COSI DA POTER POPOLARE L'ARRAY
                id_ordine++;
                OrdineBean order = new OrdineBean();
                float totale = 0;
                float prezzo = 0;

                //CICLO PER POPOLARE ARRAYLIST DEGLI ORDERPRODUCT
                ArrayList<OrderProductBean> products = new ArrayList<>();
                for (CartProduct cp : cart.getProducts()){
                    OrderProductBean prodotto = new OrderProductBean();
                    prodotto.setId_ordine(id_ordine);
                    prodotto.setId_prodotto(cp.getProduct().getId());
                    prodotto.setPrezzo(cp.getProduct().getPrezzo());
                    prodotto.setIVA(cp.getProduct().getIva());
                    prodotto.setQuantita(cp.getQuantity());
                    products.add(prodotto);
                    prezzo = cp.getProduct().getPrezzo();
                    totale = totale + (prezzo * cp.getQuantity());
                }

                order.setProducts(products);

                order.setId(id_ordine);
                order.setClient(utente);
                if (spedizione.equalsIgnoreCase("Express")){

                    totale = totale + 5;
                }

                order.setPrezzo_totale(totale);
                order.setDestinatario(destinatario);
                order.setMetodo_di_pagamento(metpag);

                try {
                    order.setCircuito(paymentmodel.doRetrieveByKey(idcarta).getCircuito());//prende il numero carta e il circuito dal paymentmethodbean usato per pagare l'ordine
                } catch (SQLException e) {                                                 //e lo inserisce nell'ordine (mantiene la storia degli ordini nel database)
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                try {
                    order.setNumero_carta(paymentmodel.doRetrieveByKey(idcarta).getNumero_carta());
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                try {
                    bean = addressmodel.doRetrieveByKey(idindirizzo);// prende l'indirizzo dall' addressbean scelto per effettuare l'ordine e lo inserisce in ordine sottoforma di stringa formattata
                } catch (SQLException e) {                         // (mantiene la storia degli ordini)
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                String indirizzospedizione = bean.getVia() + "," + bean.getCitta();
                order.setIndirizzo_di_spedizione(indirizzospedizione);

                SecureRandom r = new SecureRandom(); // uso di un Secure Random per generare il numero di tracking
                int low = 100000;
                int high = 10000000;
                String result = Integer.toString(r.nextInt(high-low) + low);
                order.setNumero_di_tracking(result);

                order.setData(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                order.setMetodo_di_spedizione(spedizione);

                try {
                    //controllo che la quantità di prodotti inserita nel carrello sia ancora disponibile
                    for (CartProduct cp : cart.getProducts()){
                        if(cp.getQuantity() > cp.getProduct().getQuantita() ){
                            check = false;
                        }
                    }
                    if (check != false){ // se tutti i prodotti sono disponibili si salva l'ordine nel database (il salvataggio degli orderproductbean viene effettuato dalla doSave dell'orderDAO)
                        orderModel.doSave(order);
                        for(CartProduct cp : cart.getProducts()){
                            ProdottoBean p = prodottoModel.doRetrieveByKey(cp.getProduct().getId());
                            prodottoModel.updateQuantity(p.getId(), p.getQuantita() - cp.getQuantity());
                        }
                    }
                    else{

                        response.sendRedirect("./ErrorPage/generalError.jsp");
                        return;
                    }


                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                //GENERAZIONE DELLA FATTURA
                SecureRandom n = new SecureRandom();
                int low1 = 1000000;
                int high2 = 9999999;
                String sdi = Integer.toString(n.nextInt(high2-low1) + low1);

                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String data_emissione = dateFormat.format(date);

                String data_scadenza = (data_emissione.substring(0, 9)+"4");

                invoice.setSdi(sdi);
                invoice.setImporto(totale);
                invoice.setData_emissione(data_emissione);
                invoice.setData_scadenza(data_scadenza);
                invoice.setStato_pagamento("Paid");
                invoice.setIva(22);
                invoice.setId(id_ordine);

                try {
                    invoicemodel.doSave(invoice);
                }  catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                //IL CARRELLO ADESSO E' VUOTO
                request.getSession().removeAttribute("cart");
                request.getSession().setAttribute("cart", null);

                response.sendRedirect("GestioneCatalogo");
                return;
            }

        }else {
            response.sendRedirect("./ErrorPage/cartError.jsp");
            return;
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

    public void sendError(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        request.setAttribute("error", "KangarooBuy ha riscontrato un problema durante il pagamento. Perfavore,compila il form correttamente e controlla i dati immessi prima di inviarlo.");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/checkout.jsp");
        dispatcher.forward(request, response);
    }
}
