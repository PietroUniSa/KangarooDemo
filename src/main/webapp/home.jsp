<%@ page import="ita.kangaroo.model.ProdottoBean" %>
<%@ page import="java.util.ArrayList" %>

<%
    ArrayList<ProdottoBean> products= (ArrayList<ProdottoBean>) request.getAttribute("prodotti");
%>

<%@include file="header.jsp" %>

    <title> Kangaroo Home </title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>
        .grid-container {
            display: grid;
            grid-template-columns: auto ;
            grid-template-rows: auto auto auto auto auto;
            gap: 60px;
            background: rgba(250,250,250);


        }

        .grid-container > div {

            font-size: 30px;
        }

        .slider-prodotti {
            width: 100%;
            height: 100%;
            overflow-x: auto;
            overflow-y: hidden;
            padding:0.5%;
            background: transparent;

        }

        .slider-container {
            display: flex;
            height: auto;


        }
        .product-card {

            flex : 0 0 20%;
            background: rgba(240,240,240);
            border: 2px solid #FF9229;
            border-radius: 20px;
            margin: 1%;
            text-align: center;
            width : 40%;
            height: 80%;
            padding : 2%;
        }

        .slider-container img {
            width: 60%;
            height: 50%;
            transition: transform .2s;
            border: 2px solid #FF9229;
            border-radius: 10px;

        }

        .slider-container img:hover {
            -ms-transform: scale(1.2); /* IE 9 */
            -webkit-transform: scale(1.2); /* Safari 3-8 */
            transform: scale(1.2);
        }

        .slider-container p,.slider-container a{
            font-family: sans-serif;
            font-size: 20px;

        }

        .product-card a:link,.product-card a:visited {
            background-color: white;
            color: black;
            border: 2px #FF9229;
            padding: 5%;
            border-radius: 20px;

            text-align: center;
            text-decoration: none;
            display: inline-block;
        }

        .product-card a:hover,.product-card a:active {
            background-color: #FF9229;
            color: white;
            padding: 5%;
            border-radius: 20px;
        }

        .content{

            text-align: center;
            background: rgba(255, 255, 255, 0.7);
            padding: 5%;
            background-image: url(img/sfondosfoc.jpg);
            background-size: cover;
            background-position: center;
            margin-left: -18px;
            margin-right: -18px;
            margin-top: -40px;

        }

        .content p{

            font-size: 110%;
            margin-left: 70%;
            color: #FF9229;
            text-shadow: -1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000;
            font-family: unset;
        }

        .content a:link,.content a:visited {

            background: rgba(255,255,255,0.1);
            color: black;
            border: 2px solid #ff9229;
            padding: 2%;
            border-radius: 40px;
            margin-left: 70%;


            text-align: center;
            text-decoration: none;
            display: inline-block;
        }

        .content a:hover,.content a:active {
            background-color: #ff9229;
            color: black;
            border-radius: 40px;
        }


        h1 {

            display: flex;
            justify-content: center;
            text-align: center;
            color: black;
            padding: 1%;
            border-top-style: double;
            border-bottom-style: double;
            border-color: #ff9229;
            font-family: sans-serif;


        }







    </style>
</head>
<body>
<div class="grid-container" id="1">
    <div class="banner" >

        <div  class="content" >

            <p>Visita il catalogo di KangarooBuy! </p>
            <a href="GestioneCatalogo"> Catalogo </a>

        </div>

    </div>

    <h1> Controlla i nostri prodotti!</h1>

    <div class="slider-prodotti">
        <div class="slider-container">
            <% for (ProdottoBean prodotto : products){%>
            <div class = "product-card">
                <img src="<%= "img/" + prodotto.getImmagine() %>" alt="${prodotto.nome}"/></p>
                <p><b><%=prodotto.getNome()%></b></p>
                <p><b><%= prodotto.getPrezzo() %> &euro;</b></p>
                <a href="<%= "DettagliS?id=" + prodotto.getId() %>"> Dettagli Prodotto</a>
            </div>

            <% } %>
        </div>
    </div>


    <p style="text-align:center; font-size: 200%"><a href="#1"><i class="fa fa-angle-double-up" aria-hidden="true"></i></a><br> Vai su!</p>

</div>
<%@include file="footer.jsp" %>
</body>
</html>