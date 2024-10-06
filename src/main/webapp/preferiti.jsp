<%@include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.CartProduct" %>
<%@ page import="ita.kangaroo.model.*" %>
<%@ page import="java.util.ArrayList" %>
<% Prefered pr = (Prefered) request.getSession().getAttribute("preferiti");
    if (pr == null) {
        pr = new Prefered();
        request.getSession().setAttribute("preferiti", pr);

    }

    String erroredisponibilita = (String)request.getAttribute("erroredisponibilita");
    String erroresoldout = (String)request.getAttribute("erroresoldout");
%>
<link rel="stylesheet" type="text/css" href="styles/tableStyle.css">
<link rel="stylesheet" type="text/css" href="styles/alertStyle.css">
<title> Carrello </title>
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
            padding: 10%;
            margin-top: 2px;
            cursor: pointer;
            border-radius: 20px;
            font-size:5px;
        }
        input[type="submit"]:hover {
            background-color: #ff9229;
            color: #fff;
            border: none;
            padding: 10%;
            margin-top: 2px;
            cursor: pointer;
            font-size:5px;
        }
        input[type="number"] {
            width: 10px;
            height: 10px;
            font-size: 8px;
        }
        #checkout-btn {
            padding: 2%;
        }
        #checkout-btn:hover {
            padding: 2%;
        }
    }
</style>
</head>
<body>
<h1> Prodotti Preferiti </h1>

<% if (erroredisponibilita != null){%>
<div><%=erroredisponibilita%></div>
<%}%>

<% if (erroresoldout != null){%>
<div><%=erroresoldout%></div>
<%}%>
<table border="1">
    <tr>
        <th> Immagine </th>
        <th> Nome </th>
        <th> Prezzo per unita </th>

    </tr>

    <% ArrayList<PreferedProduct> cartProd = pr.getProducts();
        for (PreferedProduct c : cartProd) { %>
    <form method="post" action="Preferiti">
        <tr>
            <td> <img src="<%= "img/" + c.getProduct().getImmagine() %>" alt="<%= c.getProduct().getNome() %>"  width="100"  height="100"> </td>
            <td> <%= c.getProduct().getNome() %> </td>
            <td> <%= c.getProduct().getPrezzo() %>&euro; <input type="hidden" name="id" value="<%= c.getProduct().getId() %>"> </td>
            <td> <input type="submit" name="action" value="Rimuovi"> <br> <input type="submit" name="action" value="Aggiungi al carrello"> <br> </td>
        </tr>
    </form>
    <% } %>
</table>

<%@include file="footer.jsp" %>
</body>
</html>
