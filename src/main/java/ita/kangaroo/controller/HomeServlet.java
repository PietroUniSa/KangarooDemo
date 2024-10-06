package ita.kangaroo.controller;

import ita.kangaroo.dao.prodottoDao;
import ita.kangaroo.model.ProdottoBean;
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

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName() );

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        prodottoDao model = new prodottoDao();


        ArrayList<ProdottoBean> prodotti = new ArrayList<ProdottoBean>();

        try {
            prodotti = model.doRetrieveAllLimit();
        } catch (SQLException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );
            response.sendRedirect("./ErrorPage/generalError.jsp");
            return;
        }

        request.setAttribute("prodotti", prodotti);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }
}