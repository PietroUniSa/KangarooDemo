<%@ page import="ita.kangaroo.model.ProdottoBean" %>
<%@include file="header.jsp" %>
<%
    ProdottoBean p = (ProdottoBean) request.getAttribute("prodotto");
    String error = (String) request.getAttribute("error");
    utenteBean client = (utenteBean) request.getSession().getAttribute("utente");
    if (client == null || !client.getEmail().equals("provoloni@example.com")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("HomeServlet");
        dispatcher.forward(request, response);
    }
%>
<title> Admin Page </title>
<link rel="stylesheet" type="text/css" href="styles/multi-formStyle.css">
<style>
    .submitContainer{
        display: flex;
        flex-direction: row;
        gap: 10px;
    }



    input[type="submit"]:hover, input[type="reset"]:hover {
        background-color: black;
        color: white;
    }

    #load, #delete {
        color: black;
        border: 2px solid black;
    }

    #load:hover, #delete:hover {
        background-color: #ff9229;
        color: black;
    }
</style>
</head>

<body>
<div class="grid-container">
    <div class="screen-container">
        <span class="title"> Admin Page </span>

        <div class="transparentCard">
            <% if (error != null) { %>
            <div class="errorNoTranslate"><%= error %></div>
            <% } %>
            <form class="formContainer" action="AdminServlet" method="post" enctype="multipart/form-data">
                <span class="subtitle">Aggiungi un prodotto</span>
                <input type="hidden" name="action" value="insert">

                <div class="inputBox">
                    <input id="name" name="name" type="text" maxlength="50" required autocomplete="off" placeholder="enter name">
                    <label for="name">Nome:</label>
                </div>

                <div class="inputBox special">
                    <label for="category">Categoria:</label>
                    <select id="category" name="category">
                        <option value="animale">Animale</option>
                        <option value="cibo">Cibo</option>
                        <option value="accessorio">Accessorio</option>
                    </select>
                </div>
                <div class="inputBox special">
                    <label for="image">Immagine:</label>
                    <input id="image" name="image" type="file" required>
                </div>

                <div class="inputBox special">
                    <label for="availability">Quantita':</label>
                    <input id="availability" name="availability" type="number" min="0" required>
                </div>

                <div class="inputBox special">
                    <label for="IVA">IVA:</label>
                    <input id="IVA" name="IVA" type="number" min="1" max="22" value="22" required>
                </div>

                <div class="inputBox special">
                    <label for="price">Prezzo:</label>
                    <input id="price" name="price" type="number" min="1" max='5000' required>
                </div>

                <div class="inputBox special">
                    <label for="description">Descrizione:</label>
                    <textarea id="description" name="description" maxlength="500" rows="3" required placeholder="enter description"></textarea>
                </div>

                <div class="submitContainer">
                    <input class="submit" type="submit" value="Add">
                    <input class="submit" type="reset" value="Reset">
                </div>
            </form>
            <form class="formContainer" action="AdminServlet" method="post">
                <span class="subtitle">aggiorna con nuove informazioni</span>
                <input type="hidden" name="action" value="load">
                <fieldset>
                    <legend> scrivi l'ID del prodotto che vuoi modificare</legend>
                    <label for="id">Id:</label>
                    <input id="id" name="id" type="number" required>
                    <input id="load" type="submit" value="Load">
                </fieldset>
            </form>

            <% if (p != null) { %>
            <form class="formContainer" action="AdminServlet" method="post">
                <input type="hidden" name="action" value="modify">
                <input type="hidden" name="idM" value="<%= p.getId() %>">

                <div class="inputBox">
                    <input name="nameM" type="text" maxlength="50" required value="<%= p.getNome() %>">
                    <label for="name">Nome:</label>
                </div>

                <div class="inputBox special">
                    <label for="categoryM">Categoria:</label>
                    <select id="categoryM" name="categoryM">
                        <option value="animale" <%= (p.getTipo() != null && p.getTipo().toString().equals("animale")) ? "selected" : "" %>>Animale</option>
                        <option value="cibo" <%= (p.getTipo() != null && p.getTipo().toString().equals("cibo")) ? "selected" : "" %>>Cibo</option>
                        <option value="accessorio" <%= (p.getTipo() != null && p.getTipo().toString().equals("accessorio")) ? "selected" : "" %>>Accessorio</option>
                    </select>
                </div>

                <div class="inputBox special">
                    <label for="image">Immagine:</label>
                    <input id="imageM" name ="imageM" type="text" maxlength="100" required value="<%=p.getImmagine() %>">
                </div>
                <div class="inputBox special">
                    <label for="availabilityM">Quantita':</label>
                    <input id="availabilityM" name="availabilityM" type="number" min="0" required value="<%= p.getQuantita() %>">
                </div>

                <div class="inputBox special">
                    <label for="IVA">IVA:</label>
                    <input name="IVAM" type="number" min="1" max="22" required value="<%= p.getIva() %>">
                </div>

                <div class="inputBox special">
                    <label for="price">Prezzo:</label>
                    <input name="priceM" type="number" min="1" max="5000" required value="<%= p.getPrezzo() %>">
                </div>

                <div class="inputBox special">
                    <label id="descriptionM">Descrizione:</label>
                    <textarea name="descriptionM" maxlength="500" rows="3" required><%= p.getDescrizione() %></textarea>
                </div>

                <div class="submitContainer">
                    <input class="submit" type="submit" value="Modify">
                </div>
            </form>

            <% } %>

            <form class="formContainer" action="AdminServlet" method="post">
                <span class="subtitle">Elimina un prodotto</span>
                <input type="hidden" name="action" value="delete">
                <fieldset>
                    <legend> Scrivi l'ID del prodotto da cancellare</legend>
                    <label for="id">Id:</label>
                    <input name="id" type="number" required>
                    <input id="delete" type="submit" value="Delete">
                </fieldset>
            </form>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>
</body>
</html>