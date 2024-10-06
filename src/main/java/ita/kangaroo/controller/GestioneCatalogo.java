package ita.kangaroo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.*;
import ita.kangaroo.dao.prodottoDao;
import ita.kangaroo.model.ProdottoBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GestioneCatalogo")

public class GestioneCatalogo extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GestioneCatalogo.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        prodottoDao model = new prodottoDao();
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        String action = request.getParameter("action");
        ArrayList<ProdottoBean> result = null;

        if (ajax) {
            if (action == null) {
                try {
                    result = (ArrayList<ProdottoBean>) model.doRetrieveAll();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
            } else if (action.equals("suggest")) {
                String keyword = request.getParameter("keyword");
                try {
                    result = model.doRetrieveAllByName(keyword);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
            } else if (action.equals("searchByCategory")) {
                String category = request.getParameter("category");
                try {
                    result = model.doRetrieveAllByCategory(category);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
            } else if (action.equals("filter")) {
                float prezzo_da = 0;
                float prezzo_a = 5000;

                try {
                    if (request.getParameter("prezzo_da") != null && !request.getParameter("prezzo_da").isEmpty()) {
                        prezzo_da = Float.parseFloat(request.getParameter("prezzo_da"));
                    }
                    if (request.getParameter("prezzo_a") != null && !request.getParameter("prezzo_a").isEmpty()) {
                        prezzo_a = Float.parseFloat(request.getParameter("prezzo_a"));
                    }
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.SEVERE, "Invalid price format", e);
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }

                String categoria = request.getParameter("categoria");

                StringBuilder sql = new StringBuilder();
                sql.append(" AND Prezzo >= ").append(prezzo_da).append(" AND Prezzo <= ").append(prezzo_a);

                if (categoria != null && !categoria.isEmpty()) {
                    sql.append(" AND Tipo = '").append(categoria).append("'");
                }

                try {
                    result = model.doRetrieveAllByKeyword("", sql.toString());
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    response.sendRedirect("./ErrorPage/generalError.jsp");
                    return;
                }
            }

            Gson gson = new Gson();
            String json = gson.toJson(result);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(json);
        } else if (action == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Catalogo.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}