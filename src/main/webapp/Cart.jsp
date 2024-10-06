<%@include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.CartProduct" %>
<%@ page import="ita.kangaroo.model.*" %>
<%@ page import="java.util.ArrayList" %>
<% Cart cart = (Cart) request.getSession().getAttribute("cart");
    if (cart == null) {
        cart = new Cart();
        request.getSession().setAttribute("cart", cart);
    }

    utenteBean user = (utenteBean) request.getSession().getAttribute("utente");
    boolean isLoggedIn = (user != null);

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
<script>
    function checkCart() {
        var isLoggedIn = <%= isLoggedIn %>;
        if (!isLoggedIn) {
            document.getElementById("login-alert").style.display = "block";
            return false;
        }

        var cartSize = <%= cart.getProducts().size() %>;
        if (cartSize === 0) {
            document.getElementById("cart-alert").style.display = "block";
            return false;
        }
        return true;
    }

    function closeAlert(alertId) {
        document.getElementById(alertId).style.display = "none";
    }
</script>
</head>
<body>
<h1> Dettagli Carrello </h1>

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
        <th> Quantita </th>
        <th> Operazioni </th>
    </tr>

    <% ArrayList<CartProduct> cartProd = cart.getProducts();
        for (CartProduct c : cartProd) { %>
    <form method="post" action="GestioneCart">
        <tr>
            <td> <img src="<%= "img/" + c.getProduct().getImmagine() %>" alt="<%= c.getProduct().getNome() %>"  width="100"  height="100"> </td>
            <td> <%= c.getProduct().getNome() %> </td>
            <td> <%= c.getProduct().getPrezzo() %>&euro; <input type="hidden" name="id" value="<%= c.getProduct().getId() %>"> </td>
            <td> <input type="number" name="quantity" value="<%= c.getQuantity() %>"> </td>
            <td> <input type="submit" name="action" value="Modifica Quantita"> <br> <input type="submit" name="action" value="Elimina dal carrello"> <br> </td>
        </tr>
    </form>
    <% } %>
</table>
<!-- FORM CON ACTION BUY-->
<form action="GestioneCart" method="post" onsubmit="return checkCart()">
    <input type="hidden" name="action" value="buy">
    <input type="submit" id="checkout-btn" value="Procedi al pagamento">
</form>

<div id="cart-alert" class="custom-alert">
    <p>Il carrello e' vuoto. Aggiungi prodotti prima di procedere al pagamento.</p>
    <button onclick="closeAlert('cart-alert')">Chiudi</button>
</div>

<div id="login-alert" class="custom-alert">
    <p>Devi essere loggato per procedere al pagamento. Effettua il <a href="LoginServlet">login</a>.</p>
    <button onclick="closeAlert('login-alert')">Chiudi</button>
</div>

<%@include file="footer.jsp" %>
</body>
</html>
