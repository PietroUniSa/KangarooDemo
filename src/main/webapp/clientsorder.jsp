<%@ include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.utenteBean" %>
<%@ page import="ita.kangaroo.model.OrdineBean" %>
<%@ page import="java.util.ArrayList" %>
<%
    utenteBean client = (utenteBean) request.getSession().getAttribute("utente");
    if (client == null) {
        response.sendRedirect("/LoginServlet");
        return;
    }

    ArrayList<OrdineBean> orders = (ArrayList<OrdineBean>) request.getAttribute("ordini");
    if (orders == null) {
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/OrdiniUtenteServlet");
        dispatcher.forward(request, response);
        return;
    }

    String dateError = (String)request.getAttribute("dateError");
    String clientError = (String)request.getAttribute("clientError");
    for (OrdineBean ordinea : orders) {
        System.out.println(ordinea.getPrezzo_totale());
    }
%>

    <link rel="stylesheet" type="text/css" href="styles/tableStyle.css">
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport" />
    <title>Ordini</title>
    <style>

        input[type="checkbox"]{
            accent-color: #9590a8;
        }

        .searchbar input[type="text"]{
            border-radius: 10px;
            padding: 2px;
            width: 30%;
            height: auto;
            border-top: solid;
            border-bottom: solid;

        }

        #adminForm{
            color: #18020C;
        }


        @media screen and (max-width: 700px) {

            img {

                width: 70px;
                height: 70px;

            }

            table, th, td {
                border: none;
                font-size: 8px;
                padding: 1%;

            }


            table th:first-child {
                border-radius: 10px 0 0 10px;
            }

            table th:last-child {
                border-radius: 0 10px 10px 0;
            }


            button {
                padding: 0 3px;
                padding-bottom: 5px;
                margin: 0 5px;
                border: 2px solid #ff9229;
                border-radius: 20px;
                background-color: #fff;
                cursor: pointer;
                text-align: center;
                letter-spacing: 1px;
                width: 25px;
                height: 15px;
                font-size: 10px;
            }

            button:hover {
                color: white;
                background-color: #ff9229;
            }

            button:active {
                transform: scale(0.9);
            }

            #adminForm {
                font-size: 10px;
            }

            input[type="text"] {
                font-size: 10px;
                width: 50px;
                height: 10px;
            }

            input[type="date"] {
                font-size: 10px;
                width: 50px;
                height: 10px;
            }


            input[type="submit"] {
                background-color: #18020C;
                color: #fff;
                border: none;
                padding: 0;
                margin-top: 2px;
                cursor: pointer;
                border-radius: 20px;
                font-size: 5px;
                width: 20px;
                height: 15px;
            }

            input[type="submit"]:hover {
                background-color: #ff9229;
                color: #fff;
                border: none;
                margin-top: 2px;
                cursor: pointer;
                font-size: 5px;


            }

            #reset:hover {
                background-color: #ff9229;
                color: #fff;
                border: none;
                padding: 2%;
                margin-top: 2px;
                cursor: pointer;
                font-size: 5px;
                transform: scale(0.6);
            }

            #reset {
                background-color: #18020C;
                color: #fff;
                border: none;
                padding: 5px;
                margin-top: 2px;
                cursor: pointer;
                border-radius: 20px;
                font-size: 5px;
                width: 20px;
                height: 15px;
            }

            @media screen and (max-width: 450px) {

                input[type="submit"]:hover {
                    background-color: #ff9229;
                    color: #fff;
                    border: none;
                    padding: 2%;
                    margin-top: 2px;
                    cursor: pointer;
                    font-size: 5px;
                    text-align: center;
                    width: 15px;
                    height: 10px;
                }


            }
        }
    </style>

</head>

<body>

    <% if (!client.getEmail().equals("provoloni@example.com")) { %>
<!-- ------------------------------------------------------ -->
<h1><%= client.getUsername() %>'s orders</h1>
<table>
    <tr>
        <th>Data Ordine</th>
        <th>Prezzo Totale</th>
        <th>Dettagli</th>
    </tr>
    <%
        for (OrdineBean order : orders) {
    %>
    <tr>
        <td><%= order.getData() %></td>
        <td><%= order.getPrezzo_totale() %>&euro;</td>
        <!-- Corrected the dynamic ID reference -->
        <td><button onclick="redirectToServlet(<%= order.getId() %>)" id="clientbutton<%= order.getId() %>"><i class="fa fa-eye" aria-hidden="true"></i></button></td>
    </tr>
    <%
        }
    %>
</table>
<% } else { %>
<!--	---------------------------------------------------------------- -->
<form action="AdminServlet" method="get">
    <input type="hidden" name="action" value="ordersNoFilter">
    <h1>Ordini KangarooBuy</h1>
</form>

<form id="adminForm" action="AdminServlet?action=orders" method="post">
    <% if (clientError != null) { %>
    <span class="errorNoTranslate"><%= clientError %></span>
    <% } %>

    <label for="cliente">Inserisci cliente</label>
    <div class="searchbar">
        <input id="cliente" name="cliente" type="text" placeholder="user" autocomplete="off">
    </div>
    <label for="orderByClient">Ordina per cliente</label>
    <input id="orderByClient" name="Order By Client" type="checkbox" value="true">
    <br>

    <label for="data_da">Inserisci la prima data</label>
    <input id="data_da" name="data_da" type="date" placeholder="yyyy/mm/dd">
    <label for="data_a">Inserisci la seconda data</label>
    <input id="data_a" name="data_a" type="date" placeholder="yyyy/mm/dd">
    <label for="orderByDate">Ordina per data</label>
    <input id="orderByDate" name="Order By Date" type="checkbox" value="true">
    <br>

    <% if (dateError != null) { %>
    <span class="errorNoTranslate"><%= dateError %></span>
    <% } %>

    <input type="submit" value="Order">
    <a href="AdminServlet?action=ordersNoFilter" id="reset">Reset</a>
</form>

<table>
    <tr>
        <th>Data Ordine</th>
        <th>Prezzo Totale</th>
        <th>Cliente</th>
        <th>Dettagli</th>
    </tr>
    <%
        for (OrdineBean order : orders) {
    %>
    <tr>
        <td><%= order.getData() %></td>
        <td><%= order.getPrezzo_totale() %>&euro;</td>
        <td><%= order.getClient().getUsername() %></td>
        <!-- Corrected the dynamic ID reference -->
        <td><button onclick="redirectToServlet2(<%= order.getId() %>)" id="adminbutton<%= order.getId() %>"><i class="fa fa-eye" aria-hidden="true"></i></button></td>
    </tr>
    <%
        }
    %>
</table>
<% } %>

<br>



<script>
    // Modified functions to accept order ID as a parameter
    function redirectToServlet(orderId) {
        window.location.href = "DettagliOrdini?ordine=" + orderId;
    }

    function redirectToServlet2(orderId) {
        window.location.href = "DettagliOrdini?ordine=" + orderId;
    }
</script>
    <%@include file="footer.jsp" %>
</body>
</html>