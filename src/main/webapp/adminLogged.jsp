<%@include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.utenteBean" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="styles/AdminStyle.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<header>
    <div class="grid-container">
        <div class= "menu">
            <button class="sidenav-btn" onclick="openNav()">&#9776;</button>

            <div id="mySidenav" class="sidenav">
                <button class="closebtn" onclick="closeNav()">&times;</button>

                <a href="HomeServlet">  Home <i class="fa fa-home" aria-hidden="true"></i> </a>
                <a href="GestioneCatalogo">  Catalogo <i class="fa fa-diamond" aria-hidden="true"></i> </a>

                <% if (clientbean == null) { %>

                <a href="LoginServlet">Login <i class="fa fa-sign-in" aria-hidden="true"></i></a><br>
                <a href="RegistrationServlet">Register <i class="fa fa-user-plus" aria-hidden="true"></i></a><br>

                <% } else if (clientbean != null && (clientbean.getEmail().equals("provoloni@example.com"))) { %>

                <a href="AdminServlet?action=ordersNoFilter">Ordini <i class="fa fa-shopping-bag" aria-hidden="true"></i></a><br>
                <a href="AdminServlet?action=clientsNoFilter">Clienti <i class="fa fa-users" aria-hidden="true"></i></a><br>
                <a href="AdminServlet">Profilo Admin <i class="fa fa-user" aria-hidden="true"></i></a><br>
                <a href="LoginServlet?action=logout">Logout <i class="fa fa-sign-out" aria-hidden="true"></i></a><br>

                <% } else {%>


                <a href="OrdiniUtenteServlet">Miei Ordini <i class="fa fa-shopping-bag" aria-hidden="true"></i></a><br>
                <a href="UtenteServlet">Profilo <i class="fa fa-user" aria-hidden="true"></i></a><br>
                <a href="LoginServlet?action=logout">Logout <i class="fa fa-sign-out" aria-hidden="true"></i></a><br>
                <% } %>
            </div>

        </div>

        <div class="logo">

            <img src="images//logo.png" class="logo" alt="logo"></a>

        </div>

        <div id="main" class="main-header">


            <% if (clientbean == null) { %>


            <a href="HomeServlet">  Home  </a>
            <a href="GestioneCatalogo">  Catalogo  </a>

            <% } %>


            <% if (clientbean != null && !clientbean.getEmail().equals("provoloni@example.com")  ) { %>


            <a href="HomeServlet">  Home  </a>
            <a href="GestioneCatalogo">  Catalogo  </a>


            <% } %>

            <% if (clientbean != null && clientbean.getEmail().equals("provoloni@example.com")) { %>


            <a href="HomeServlet">  Home  </a>
            <a href="GestioneCatalogo">  Catalogo  </a>


            <% } %>

        </div>


    </div>
</header>
<script>
    function openNav() {
        document.getElementById("mySidenav").style.width = "250px";

    }

    function closeNav() {
        document.getElementById("mySidenav").style.width = "0";
        document.getElementById("main").style.marginLeft= "0";
    }
</script>

<%@include file="footer.jsp" %>