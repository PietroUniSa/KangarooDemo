package ita.kangaroo.controller;

import ita.kangaroo.dao.utenteDao;
import ita.kangaroo.model.utenteBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName() );
    private static utenteDao model = new utenteDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if(action!=null && action.equalsIgnoreCase("logout")){ // azione di logout
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect("HomeServlet");
            return;
        }

        if (action == null || action.equals("") ){

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Login.jsp");
            dispatcher.forward(request, response);
            return;
        }else if(action.equals("login")){

            utenteBean utente = null;
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            try {
                utente = model.doRetrieveByEmailAndPassword(email, password);
            } catch (SQLException e) {

                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("./ErrorPage/loginError.jsp");
                return;
            }

            if(utente == null){ // se si inseriscono le credenziali sbagliate, si viene rediretti alla login error

                response.sendRedirect("./ErrorPage/loginError.jsp");
                return;
            }
            else {
                request.getSession().setAttribute("utente", utente); // set dell'attributo utente nella sessione

                if (utente.getEmail().equals("provoloni@example.com")) {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AdminServlet");
                    dispatcher.forward(request, response);
                    return;
                }

            }

            response.sendRedirect("HomeServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }
}
