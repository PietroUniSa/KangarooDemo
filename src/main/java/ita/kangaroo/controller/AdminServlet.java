package ita.kangaroo.controller;

import ita.kangaroo.dao.OrdineDao;
import ita.kangaroo.dao.prodottoDao;
import ita.kangaroo.dao.utenteDao;
import ita.kangaroo.model.OrdineBean;
import ita.kangaroo.model.ProdottoBean;
import ita.kangaroo.model.utenteBean;
import ita.kangaroo.model.tipo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/AdminServlet")
@MultipartConfig
public class AdminServlet extends HttpServlet {
    private static final String CARTELLA_UPLOAD = "img";
    private static final String PATH_UPLOAD = "C:/Users/cotic/OneDrive/Desktop/KangarooDemo/src/main/webapp/img";


    static OrdineDao orderModel = new OrdineDao();
    static prodottoDao model = new prodottoDao();
    static utenteDao clientModel = new utenteDao();

    private static final Logger LOGGER = Logger.getLogger(AdminServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        utenteBean client = (utenteBean) request.getSession().getAttribute("utente");
        if (client == null || !client.getEmail().equals("provoloni@example.com")) {
            response.sendRedirect("HomeServlet");
            return;
        }

        String action = request.getParameter("action");
        ArrayList<OrdineBean> orders = new ArrayList<OrdineBean>();
        ArrayList<utenteBean> clients = new ArrayList<utenteBean>();

        //se l'azione è nulla o è una stringa vuota, vuol dire che si sta accedendo alla pagina stessa (anche da url) e si viene ridirezionati
        if(action == null || action.equals("")){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin.jsp");
            dispatcher.forward(request, response);
            return;
        }

        //azione di inserimento di un nuovo prodotto
        if (action.equals("insert")) {

            //UPLOAD IMMAGINI
            Part filePart = request.getPart("image");
            String nome = request.getParameter("name");
            String estensione ="jpg";
            //GENERIAMO IL NOME DELL'IMMAGINE
            String fileName = nome + "." + estensione;
            //DIRECTORY DELL'UTENTE CORRENTE
            System.getProperty("user.dir");
            //DESTINAZIONE IMMAGINE DA SALVARE
            String destinazione = System.getProperty("user.dir") + File.separator + "webapps" + File.separator + "KangarooDemo_war" + File.separator + CARTELLA_UPLOAD + File.separator + fileName;
            System.out.println(destinazione);
            Path pathDestinazione = Path.of(destinazione);
            String filePath = PATH_UPLOAD + File.separator + fileName;

            try (InputStream input = filePart.getInputStream();
                 OutputStream output = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                System.out.println("File " + fileName + " salvato correttamente in " + PATH_UPLOAD);
            } catch (IOException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il salvataggio del file.");
                return;
            }



            // se un file con quel nome esiste già, gli cambia nome
            for (int i = 2; Files.exists(pathDestinazione); i++) {
                String newFileName = i + "_" + nome + "." + estensione;
                destinazione = System.getProperty("user.dir") + File.separator + "webapps" + File.separator + "KangarooDemo_war" + File.separator + CARTELLA_UPLOAD + File.separator+ newFileName;
                pathDestinazione = Path.of(destinazione);
            }

            InputStream fileInputStream = filePart.getInputStream();
            // crea CARTELLA_UPLOAD, se non esiste
            Files.createDirectories(pathDestinazione.getParent());

            // scrive il file
            Files.copy(fileInputStream, pathDestinazione);
            request.setAttribute("uploaded", destinazione);

            //controllo e set di tutti i parametri destinati al JewelBean prima di essere salvato nel database
            int availability;
            float IVA;
            float price;

            if(request.getParameter("availability").equals(""))
                availability = 0;
            else
                availability = Integer.parseInt(request.getParameter("availability"));

            if(request.getParameter("IVA").equals(""))
                IVA = 0;
            else
                IVA = Float.parseFloat(request.getParameter("IVA"));

            if(request.getParameter("price").equals(""))
                price = 0;
            else
                price = Float.parseFloat(request.getParameter("price"));


            String category = request.getParameter("category");
            String description = request.getParameter("description");

            if(nome==null || !nome.matches("^[A-Za-z ]+$")){

                sendError(request, response);
                return;
            }
            if(category==null || category.equals("") || (!category.equals("animale") && !category.equals("cibo") && !category.equals("accessorio") && !category.equals("Bracelet"))){

                sendError(request, response);
                return;
            }
            if(availability==0 || !(availability > 0 && availability < 100)){

                sendError(request, response);
                return;
            }
            if(IVA==0 || !(IVA > 0 && IVA < 100)){

                sendError(request, response);
                return;
            }
            if(price==0 || !(price > 0 && price <= 5000)){

                sendError(request, response);
                return;
            }
            if(description==null || !description.matches("^[a-zA-Z0-9\\s\\p{P}]{1,500}$")){

                sendError(request, response);
                return;
            }

            // Salvataggio del prodotto nel database
            ProdottoBean prodotto = new ProdottoBean();
            prodotto.setNome(nome);
            prodotto.setTipo(ita.kangaroo.model.tipo.valueOf(request.getParameter("category")));
            prodotto.setQuantita(Integer.parseInt(request.getParameter("availability")));
            prodotto.setImmagine(fileName);
            prodotto.setIva(Float.parseFloat(request.getParameter("IVA")));
            prodotto.setPrezzo(Float.parseFloat(request.getParameter("price")));
            prodotto.setDescrizione(request.getParameter("description"));

            try {
                model.doSave(prodotto);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
                return;
            }
        }

        //azione di caricamento di un prodotto dal suo id, avviene prima di poterlo modificare
        if ("load".equals(action)) {
            ProdottoBean prodottoToModify;
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                LOGGER.log(Level.INFO, "ID ricevuto: " + id);

                prodottoToModify = model.doRetrieveByKey(id);
                LOGGER.log(Level.INFO, "Prodotto recuperato: " + (prodottoToModify != null ? prodottoToModify.toString() : "null"));

                if (prodottoToModify == null) {
                    // Prodotto non trovato
                    request.setAttribute("error", "Prodotto con ID " + id + " non trovato.");
                    LOGGER.log(Level.INFO, "Prodotto con ID " + id + " non trovato.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/admin.jsp");
                    dispatcher.forward(request, response);
                } else {
                    // Prodotto trovato, imposta l'attributo prodotto
                    request.setAttribute("prodotto", prodottoToModify);
                    LOGGER.log(Level.INFO, "Prodotto trovato: " + prodottoToModify);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/admin.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (NumberFormatException | SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
            }
        }


        //vengono presi dalla request tutti i parametri, da un form, controllati e settati
        if (action.equals("modify")) {
            ProdottoBean prodotto = new ProdottoBean();

            int idM = 0;
            int QuantitaM = 0;
            float IVAM = 0;
            float PrezzoM = 0;

            try {
                idM = Integer.parseInt(request.getParameter("idM"));
                QuantitaM = Integer.parseInt(request.getParameter("availabilityM"));
                IVAM = Float.parseFloat(request.getParameter("IVAM"));
                PrezzoM = Float.parseFloat(request.getParameter("priceM"));
            } catch (NumberFormatException e) {
                sendError(request, response);
                return;
            }

            String nameM = request.getParameter("nameM");
            String categoryM = request.getParameter("categoryM");
            String descriptionM = request.getParameter("descriptionM");


            // Controllo dei parametri nulli e invio di errori
            if (nameM == null || !nameM.matches("^[A-Za-z ]+$")) {
                System.out.println("Errore: nameM non valido");
                sendError(request, response);
                return;
            }
            if (categoryM == null) {
                sendError(request, response);
                return;
            }
            if (descriptionM == null || !descriptionM.matches("^[a-zA-Z0-9\\s\\p{P}]{1,500}$")) {
                sendError(request, response);
                return;
            }

            //Converti il tipo
            tipo tipoM;
            try {
                tipoM = tipo.valueOf(categoryM);
            } catch (IllegalArgumentException e) {
                sendError(request, response);
                return;
            }

            prodotto.setId(idM);
            prodotto.setNome(nameM);
            prodotto.setTipo(tipoM);
            prodotto.setQuantita(QuantitaM);
            prodotto.setImmagine(request.getParameter("imageM"));
            prodotto.setIva(IVAM);
            prodotto.setPrezzo(PrezzoM);
            prodotto.setDescrizione(descriptionM);

            // Effettua la modifica chiamando il DAO
            try {
                model.doModify(prodotto);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
                return;
            }
        }
        if (action.equals("delete")) {
            ProdottoBean prodottoToDelete;
            boolean forwardToAdmin = false;
            String errorMessage = null;

            try {
                int id = Integer.parseInt(request.getParameter("id"));
                LOGGER.log(Level.INFO, "ID ricevuto: " + id);

                prodottoToDelete = model.doRetrieveByKey(id);
                LOGGER.log(Level.INFO, "Prodotto recuperato: " + (prodottoToDelete != null ? prodottoToDelete.toString() : "null"));

                if (prodottoToDelete == null) {
                    // Prodotto non trovato
                    errorMessage = "Prodotto con ID " + id + " non trovato.";
                    LOGGER.log(Level.INFO, errorMessage);
                    forwardToAdmin = true;
                } else {
                    // Prodotto trovato, tentativo di eliminazione
                    try {
                        model.doDelete(id);
                    } catch (SQLException e) {
                        if (e.getMessage().contains("Cannot delete product, it is referenced in composizione.")) {
                            errorMessage = "Non è possibile eliminare il prodotto poiché è presente in uno o più ordini.";
                            forwardToAdmin = true;
                        } else {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                            response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
                            return;
                        }
                    }
                }
            } catch (NumberFormatException | SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
                return;
            }

            if (forwardToAdmin) {
                request.setAttribute("error", errorMessage);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/admin.jsp");
                try {
                    dispatcher.forward(request, response);
                } catch (IOException | ServletException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin.jsp");
            }
        }
        //azione che determina la vista di tutti gli ordini effettuati
        if (action.equals("ordersNoFilter")) {
            try {
                orders = orderModel.doRetrieveAll();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
                return;
            }

            request.setAttribute("ordini", orders);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientsorder.jsp");
            dispatcher.forward(request, response);
            return;
        }

        //azione di ordinamento (due tipi: per data, per cliente) per la clientorders.jsp
        if (action.equals("orders")){

            //ordinamento per cliente
            if (Boolean.parseBoolean(request.getParameter("Order By Client"))== true){

                String user = request.getParameter("cliente");

                try {
                    orders = orderModel.doRetrieveByClient(user);
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                }

                //controllo che l'utente inserito abbia effettuato degli ordini
                if(orders.size()==0){
                    request.setAttribute("clientError", "Questo utente non ha ordini salvati");

                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientsorder.jsp");
                    dispatcher.forward(request, response);
                    return;
                }


            }//ordinamento per data
            if(Boolean.parseBoolean(request.getParameter("Order By Date"))== true){

                String data_da = request.getParameter("data_da");
                String data_a = request.getParameter("data_a");

                if (data_da.compareTo(data_a)< 0){
                    try {
                        orders = orderModel.DateOrders(data_da,data_a);
                    } catch (SQLException e) {
                        LOGGER.log( Level.SEVERE, e.toString(), e );
                    }
                }
                else { //se la data "da" è più recente della data "a"
                    request.setAttribute("dateError", "Inserisci date valide");
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientsorder.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            //ordinamento sia per data che per utente
            if ((Boolean.parseBoolean(request.getParameter("Order By Date"))== true) && (Boolean.parseBoolean(request.getParameter("Order By Client"))== true)){

                String user = request.getParameter("cliente");
                String data_da = request.getParameter("data_da");
                String data_a = request.getParameter("data_a");
                try {
                    orders = orderModel.ClientDateOrders(user,data_da,data_a);
                } catch (SQLException e) {
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AdminServlet?action=ordersNoFilter");
                    dispatcher.forward(request, response);
                    return;
                }
            }


            request.setAttribute("ordini", orders);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientsorder.jsp");
            dispatcher.forward(request, response);
            return;

        }
        //action di ordinamento dei clienti senza alcun filtro per la client.jsp
        if (action.equals("clientsNoFilter")){

            try {
                clients = clientModel.doRetrieveAll();
            } catch (SQLException e) {
                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
                return;
            }


            request.setAttribute("clienti", clients);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cliente.jsp");
            dispatcher.forward(request, response);

            return;
        }

        if (action.equals("ByClient")){ //ordinamento per un particolare utente

            String user = request.getParameter("cliente");
            utenteBean person = null;

            try {
                person = (clientModel.doRetrieveByKey(user));
            } catch (SQLException e) {

                LOGGER.log( Level.SEVERE, e.toString(), e );
                response.sendRedirect(request.getContextPath() + "/ErrorPage/generalError.jsp");
                return;

            }
            //controllo che l'utente esista
            if (person==null) {
                request.setAttribute("clientError", "Questo utente non esiste");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cliente.jsp");
                dispatcher.forward(request, response);
                return;
            }

            clients.add(person);
            request.setAttribute("clienti", clients);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cliente.jsp");
            dispatcher.forward(request, response);
            return;
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin.jsp");
        dispatcher.forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }

    public void sendError(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        request.setAttribute("error", "Kangaroo ha incontrato un problema con la sottomissione del form, per favore riprova.");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin.jsp");
        dispatcher.forward(request, response);
    }

}