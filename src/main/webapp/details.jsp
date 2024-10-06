<%@ include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.ProdottoBean" %>


<%
    ProdottoBean p = (ProdottoBean) request.getAttribute("detailed");
    String erroresoldout2 = (String)request.getAttribute("erroresoldout2");
%>
<title>Dettagli Prodotto</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<style>
    * {
        box-sizing: border-box;
    }
    .grid-container {
        display: grid;
        grid-template-columns: auto;
        grid-template-rows: auto auto auto auto;
        gap: 30px;
        background: rgba(250,250,250);
    }
    .grid-container > div {
        text-align: center;
        font-family: "Times New Roman", serif, sans-serif;
        font-size:20px;
    }
    .main-container {
        width: 100%;
        height: 100%;
    }
    .main {
        display: flex;
        flex-wrap: wrap;
        flex-direction: row;
        justify-content: center;
        height: auto;
        padding: 2%;
    }
    .image {
        width: 45%;
        height: auto;
        padding: 1%;
        margin:1%;
    }
    .image img {
        width: 100%;
        height: 100%;
        border: 1px solid #ff9229;
        border-radius: 5px;
        transition: transform .5s;
    }
    .image img:hover {
        -ms-transform: scale(1.1); /* IE 9 */
        -webkit-transform: scale(1.1); /* Safari 3-8 */
        transform: scale(1.1);
    }
    .information {
        width: 45%;
        height: auto;
        padding:2%;
        text-align: center;
        color: #ff9229;
        margin:2%;
    }
    .title h1 {
        width: 100%;
        border-top-style: double;
        border-bottom-style: double;
        border-color: #ff9229;
        padding: 5%;
        color: #ff9229;
    }
    .main a:link, .main a:visited {
        background-color: white;
        color: black;
        border: 2px solid #ff9229;
        padding: 2%;
        border-radius: 20px;
        margin:1%;
        text-align: center;
        text-decoration: none;
        display: inline-block;
    }
    .main a:hover, .main a:active {
        background-color: #ff9229;
        color: white;
        padding: 2%;
        margin:1%;
        border-radius: 20px;
        display: inline-block;
    }
    @media screen and (max-width: 800px) {
        .main {
            flex-direction: column;
            justify-content: center;
        }
        .image {
            width: 100%;
        }
        .information {
            width: 100%;
        }
    }
</style>
</head>
<body>
<div class="title">
    <h1><%=p.getNome() %></h1>
</div>

<div class="main-container">

    <div class="main">

        <div class="image">

            <a target='_blank' href="<%="img/" + p.getImmagine() %>">

                <img src="<%= "img/" + p.getImmagine() %>" alt="${prodotto.nome}"/></p>
            </a>

        </div>


        <div class="information">



            <h3>Descrizione</h3>
            <p><%=p.getDescrizione() %></p>


            <h3>Tipo</h3>
            <p><%=p.getTipo() %></p>

            <h3>Quantita</h3>
            <p><%=p.getQuantita() %></p>

            <h3>Prezzo</h3>
            <p><%=p.getPrezzo() %>&euro;</p>



            <%if (!(clientbean != null && clientbean.getEmail().equals("provoloni@example.com"))){ %>


            <% if (erroresoldout2 != null){%>
            <div style="color:red;"><%=erroresoldout2%></div>
            <%}%>
            <br>
            <a href="GestioneCart"> Cart </a>

            <%  if (p.getQuantita() > 0){ %>
            <a href="GestioneCart?action=add&id=<%=p.getId()%>"> Aggiungi al carrello </a>

            <% } %>


            <% } %>

            <a href="GestioneCatalogo"> Torna Indietro </a>

        </div>

    </div>

</div>
<%@ include file="footer.jsp" %>
</body>
</html>