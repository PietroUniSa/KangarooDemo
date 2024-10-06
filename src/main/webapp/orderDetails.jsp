<%@ include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.OrdineBean" %>
<%@ page import="ita.kangaroo.dao.OrdineDao" %>
<%@ page import="ita.kangaroo.dao.prodottoDao" %>
<%@ page import="ita.kangaroo.model.OrderProductBean" %>
<%@ page import="ita.kangaroo.model.ProdottoBean" %>

<%
    OrdineBean order = (OrdineBean) request.getAttribute("detailedOrder");
    OrdineDao model = new OrdineDao();
    prodottoDao prodModel = new prodottoDao();

%>

<!-- STAMPA: nome, immagine, personalizzato, quantità, prezzo -->

<link rel="stylesheet" type="text/css" href="styles/tableStyle.css">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<title>Order Details</title>
<style>

    @media screen and (max-width: 700px){

        img{

            width:70px;
            height:70px;

        }

        table, th, td{
            border: none;
            font-size: 8px;
            padding:1%;

        }


        table th:first-child{
            border-radius:10px 0 0 10px;
        }

        table th:last-child{
            border-radius:0 10px 10px 0;
        }

        input[type="submit"] {
            background-color: #18020C;
            color: #fff;
            border: none;
            cursor: pointer;
            border-radius: 20px;
            font-size:8px;
            width: 80px;
            height: 20px;
        }

        input[type="submit"]:hover {
            background-color: #ff9229;
            color: #fff;
            border: none;
            padding: 5%;
            margin-top: 2px;
            cursor: pointer;
            font-size:5px;
        }

        #goBack {
            margin-top: 5px;
            cursor: pointer;
            border-radius: 20px;
            text-decoration: none;
            font-size:8px;
            width: 20px;
            height: 10px;
        }

    }

</style>

</head>
<body>
<h1>
    Data Ordine: <%=order.getData() %>
</h1>


<div>

    <div>
        <p>
            Prezzo totale  <%=order.getPrezzo_totale()%> &euro;
        </p>

    </div>

    <div >
        <!-- QUA STA LA ROBA DELLA DATA DI CONSEGNA -->

        <div >
            <table>
                <tr>
                    <th>Nome</th>
                    <th>Immagine</th>
                    <th>Quantita</th>
                    <th>Prezzo</th>
                </tr>
                <%

                    for ( OrderProductBean prodotto : order.getProducts() ){
                        ProdottoBean prod = prodModel.doRetrieveByKey(prodotto.getId_prodotto());

                %>
                <tr>
                    <td><%=prod.getNome() %></td>
                    <td><img src="<%= "img/" + prod.getImmagine() %>"  width="70"  height="70"></td>
                    <td><%=prodotto.getQuantita() %></td>
                    <td><%=prodotto.getPrezzo() %> &euro;</td>

                </tr>

                <%	} %>
            </table>

            <form action="DettagliOrdini?action=viewInvoice" method="post">
                <input type="hidden" name="idOrder" value="<%= order.getId() %>" >
                <input type="submit" value="View Invoice">
            </form>

        </div>
    </div>

</div>

<a href="OrdiniUtenteServlet" id="goBack"><i class="fa fa-angle-double-left" aria-hidden="true"></i>Go back</a>

<%@include file="footer.jsp" %>
</body>
</html>