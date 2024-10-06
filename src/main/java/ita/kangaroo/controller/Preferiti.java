package ita.kangaroo.controller;

import ita.kangaroo.dao.prodottoDao;
import ita.kangaroo.model.Prefered;
import ita.kangaroo.model.ProdottoBean;
import ita.kangaroo.model.utenteBean;
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

@WebServlet("/Preferiti")
public class Preferiti extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final prodottoDao model = new prodottoDao();
    private static final Logger LOGGER = Logger.getLogger(Preferiti.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Prefered preferiti = (Prefered) session.getAttribute("preferiti");
        if (preferiti == null) {
            preferiti = new Prefered();
            session.setAttribute("preferiti", preferiti);
        }

        utenteBean client = (utenteBean) session.getAttribute("utente");

        if (client != null && "provoloni@example.com".equals(client.getEmail())) {
            response.sendRedirect("HomeServlet");
            return;
        }

        String id = request.getParameter("id");
        String action = request.getParameter("action");
        boolean isRedirected = false;

        if (action == null) {
            request.setAttribute("preferiti", preferiti);
            response.sendRedirect(request.getContextPath() + "/preferiti.jsp");
            return;
        }

        try {
            if ("add".equals(action)) {
                int productId = Integer.parseInt(id);
                ProdottoBean product = model.doRetrieveByKey(productId);
                if (product != null) {
                    preferiti.addPreferito(product);
                    session.setAttribute("preferiti", preferiti);
                }
                isRedirected = true;
                response.sendRedirect(request.getContextPath() + "/preferiti.jsp");
            } else if ("Rimuovi".equals(action)) {
                int productId = Integer.parseInt(id);
                ProdottoBean product = model.doRetrieveByKey(productId);
                if (product != null) {
                    preferiti.removePreferito(product);
                    session.setAttribute("preferiti", preferiti);
                }
                isRedirected = true;
                response.sendRedirect(request.getContextPath() + "/preferiti.jsp");
            } else if ("Aggiungi al carrello".equals(action)) {
                isRedirected = true;
                response.sendRedirect("GestioneCart?action=add&id=" + id);
            }
        } catch (NumberFormatException | SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            if (!isRedirected) {
                response.sendRedirect("./ErrorPage/generalError.jsp");
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
