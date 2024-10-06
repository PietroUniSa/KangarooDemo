<%@ page import="ita.kangaroo.model.utenteBean" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="styles/style.css">
</head>
<body>
<%
  utenteBean clientbean = (utenteBean) request.getSession().getAttribute("utente");
%>
<nav id="nav">
  <div class="navTop">
    <div class="navItem">
      <a href="HomeServlet"><img src="./img/kang.png" alt="" width="90" height="90"></a>
    </div>
    <div class="navItem">
      <div class="search">
        <input type="text" id="globalSearchInput" placeholder="Cerca..." class="searchInput">
        <img src="./img/search.png" width="35" height="35" alt="" class="searchIcon" onclick="performSearch()">
        <div id="searchResults" class="searchResults"></div>
      </div>
    </div>
    <div class="navItem">
      <div class="navMenu">
        <% if (clientbean == null) { %>
        <h3 class="menuItem"><a href="LoginServlet">Login</a></h3>
        <h3 class="menuItem"><a href="RegistrationServlet">Register</a></h3>
        <h3 class="menuItem"><a href="Preferiti">Preferiti</a></h3>
        <% } else if (clientbean != null && clientbean.getEmail().equals("provoloni@example.com")) { %>
        <h3 class="menuItem"><a href="AdminServlet?action=ordersNoFilter">Ordini</a></h3>
        <h3 class="menuItem"><a href="AdminServlet?action=clientsNoFilter">Clienti</a></h3>
        <h3 class="menuItem"><a href="AdminServlet">Profilo Admin</a></h3>
        <h3 class="menuItem"><a href="LoginServlet?action=logout">Logout</a></h3>
        <% } else { %>
        <h3 class="menuItem"><a href="OrdiniUtenteServlet">Miei Ordini</a></h3>
        <h3 class="menuItem"><a href="UtenteServlet">Profilo</a></h3>
        <h3 class="menuItem"><a href="Preferiti">Preferiti</a></h3>
        <h3 class="menuItem"><a href="LoginServlet?action=logout">Logout</a></h3>
        <% } %>
      </div>
    </div>
    <div class="navItem">
      <a href="Cart.jsp"><img src="./img/cart.png" width="70" height="70" alt=""></a>
    </div>
  </div>
</nav>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
  function performSearch() {
    var keyword = $('#globalSearchInput').val();
    if (keyword.trim() !== '') {
      $.ajax({
        url: 'GestioneCatalogo',
        method: 'GET',
        data: { action: 'suggest', keyword: keyword },
        success: function(response) {
          displaySearchResults(response);
        },
        error: function() {
          alert("Errore nella ricerca dei prodotti");
        }
      });
    }
  }

  function displaySearchResults(products) {
    var resultsContainer = $('#searchResults');
    resultsContainer.empty();
    if (products.length > 0) {
      products.forEach(function(product) {
        resultsContainer.append(
                '<div class="resultItem" onclick="goToProduct(' + product.id + ')">' +
                '<h3>' + product.nome + '</h3>' +
                '<p>Prezzo: ' + product.prezzo + '€</p>' +
                '</div>'
        );
      });
      resultsContainer.addClass('visible');
    } else {
      resultsContainer.removeClass('visible');
    }
  }

  function goToProduct(productId) {
    window.location.href = 'DettagliS?id=' + productId;
  }

  $(document).ready(function() {
    $('#globalSearchInput').on('input', function() {
      performSearch();
    });

    $(document).click(function(event) {
      if (!$(event.target).closest('.search').length) {
        $('#searchResults').removeClass('visible');
      }
    });
  });
</script>
</body>
</html>
