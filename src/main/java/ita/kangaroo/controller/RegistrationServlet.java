package ita.kangaroo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;


import com.google.gson.Gson;
import ita.kangaroo.dao.AddressDao;
import ita.kangaroo.dao.utenteDao;
import ita.kangaroo.model.AddressBean;
import ita.kangaroo.model.utenteBean;
import ita.kangaroo.model.tipo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {

    static AddressDao addressmodel = new AddressDao();
    static utenteDao model = new utenteDao();
    static utenteDao clientmodel = new utenteDao();


    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class.getName() );

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        utenteBean cliente = new utenteBean();

        String action = request.getParameter("action");
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if(action == null){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/registration.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if(action != null && ajax){ //ajax è una variabile necessaria a distinguere le richieste di ajax e le richieste normali della jsp
            boolean bol;
            utenteBean result = null;
            if (action.equalsIgnoreCase("check") ){ // si controlla se è già presente questo username nel database
                try {
                    result = clientmodel.doRetrieveByKey(request.getParameter("username"));
                }catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

            }else  if (action.equalsIgnoreCase("checkemail")){ // si controlla se è già presente questa email nel database
                try {
                    result = clientmodel.doRetrieveByEmail(request.getParameter("email"));
                }catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
            }
            if (result != null) bol=true;
            else bol=false;

            Gson gson = new Gson();
            String json = gson.toJson(bol);

            response.setContentType("application/json"); // si inserisce la risposta in formato gson nella response
            PrintWriter out = response.getWriter();
            out.write(json);
        }
        else if(!ajax && action.equals("register")){ // controllo dei form server side della registration.jsp
            int result = 0;
            String username = request.getParameter("username");
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String indirizzo = request.getParameter("indirizzo");
            String citta = request.getParameter("citta");
            String provincia = request.getParameter("provincia");
            String cap = request.getParameter("cap");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if(username==null || !username.matches("^[a-zA-Z0-9_-]{6,20}$")){
                sendError(request, response);
                return;
            }
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
            if(password==null || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")){
                sendError(request, response);
                return;
            }

            cliente.setUsername(username);
            cliente.setNome(nome);
            cliente.setCognome(cognome);
            cliente.setVia(indirizzo);
            cliente.setCitta(citta);
            cliente.setProvincia(provincia);
            cliente.setCap(cap);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);
            cliente.setPassword(password);
            cliente.setTipo(tipo.cliente);
            AddressBean indirizzobase = new AddressBean();  // si estrae l'idirizzo da cliente appena registrato e si inserisce nella tabella Indirizzo con chiave esterna sull'username

            indirizzobase.setVia(indirizzo);
            indirizzobase.setCitta(citta);
            indirizzobase.setCAP(cap);
            indirizzobase.setUsername(username);

            try {
                result = model.doSave(cliente);
            } catch (SQLException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("./ErrorPage/generalError.jsp");
                return;
            }

            if(result == 0){
                response.sendRedirect("./ErrorPage/loginError.jsp");
                return;
            }else{
                try {
                    addressmodel.doSave(indirizzobase); // se l'inserimento del cliente è andato a buon fine si inserisce l'indirizzo nel database
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                response.sendRedirect(request.getContextPath() + "/LoginServlet");

            }

        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }

    public void sendError(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        request.setAttribute("error", "KangarooBuy encountered a problem during your registration. Please, try to fill up the form correctly and check your data before submitting.");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/registration.jsp");
        dispatcher.forward(request, response);
    }
}