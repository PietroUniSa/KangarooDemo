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
import java.util.logging.Level;
import java.util.logging.Logger;
@WebServlet("/DettagliS")
public class DettagliS extends HttpServlet {

        private static final Logger LOGGER = Logger.getLogger(DettagliS.class.getName() );

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            //viene preso un prodotto in base al suo id, preso come parametro della request
            prodottoDao model = new prodottoDao();

            String id = request.getParameter("id");
            if (id == null || Integer.parseInt(id)==0){
                response.sendRedirect("GestioneCatalogo");
                return;
            }

            ProdottoBean p = new ProdottoBean();

            try {
                p = model.doRetrieveByKey(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("./ErrorPage/generalError.jsp");
                return;
            } catch (SQLException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect("./ErrorPage/generalError.jsp");
                return;
            }

            if (p.getQuantita() <= 0) { //se il prodotto ha la quantità settata a 0 allora viene settato un attributo apposito

                request.setAttribute("erroresoldout2", "Siamo spiacenti ma questo prodotto e' terminato");
            }

            request.setAttribute("detailed", p);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/details.jsp");//dispatch alla pagina dedicata
            dispatcher.forward(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
            doGet(request, response);
        }
    }


