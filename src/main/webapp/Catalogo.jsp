<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Catalogo Prodotti</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/catalogStyle.css">
    <style>
        .grid-container {
            display: grid;
            grid-template-columns: 20% 80%;
            grid-template-rows: auto;
            gap: 10px;
            background-color: white;
            padding: 10px;
            font-family: sans-serif;
        }

        .grid-container > div {
            text-align: center;
            padding: 20px 5px;
            font-size: 20px;
        }

        .catalogo {
            display: flex;
            flex-wrap: wrap;
            font-size: 20px;
            font-family: sans-serif;
        }

        .product {
            border: 1px solid #ddd;
            padding: 10px;
            margin: 10px;
            width: calc(33.333% - 20px);
            box-sizing: border-box;
        }

        .product h3 {
            margin: 0;
            color: #ff9229;
        }

        body > h1 {
            text-align: center;
        }

        .addToCart, .viewDetails, .addToFavorites {
            display: inline-block;
            margin-top: 10px;
            padding: 5px 10px;
            border: 1px solid black;
            border-radius: 5px;
            text-decoration: none;
            color: black;
            font-size: 14px; /* Diminuisce la dimensione del font */
            transition: background-color 0.3s, color 0.3s;
        }

        .addToCart:hover, .viewDetails:hover, .addToFavorites:hover {
            background-color: black;
            color: white;
        }

        .addToFavorites {
            border-color: black;
        }

        .addToFavorites:hover {
            background-color: black;
            color: white;
        }

        #filterForm {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .filterInput {
            flex: 1;
            height: 40px;
            font-size: 16px;
            padding: 0 15px;
            border: 1px solid #ccc;
            border-radius: 30px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            outline: none;
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        .filterInput:focus {
            border-color: #ff9229;
            box-shadow: 0 0 5px #ff9229;
        }

        .filterButton {
            height: 40px;
            font-size: 16px;
            padding: 0 20px;
            background-color: #ff9229;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            transition: background-color 0.3s, box-shadow 0.3s;
        }

        .filterButton:hover {
            background-color: black;
            color: white;
        }

        .filterButton:focus {
            outline: none;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }
    </style>
    <script>
        $(document).ready(function() {
            // Carica tutto il catalogo al caricamento della pagina
            loadCatalogo();

            // Funzione per caricare tutto il catalogo
            function loadCatalogo() {
                $.ajax({
                    url: 'GestioneCatalogo',
                    method: 'GET',
                    success: function(response) {
                        displayProducts(response);
                    },
                    error: function() {
                        alert("Errore nel caricamento del catalogo");
                    }
                });
            }

            function displayProducts(products) {
                $('#productContainer').empty();
                $.each(products, function(index, product) {
                    $('#productContainer').append(
                        '<div class="product">' +
                        '<h3>' + product.nome + '</h3>' +
                        '<p>Prezzo: ' + product.prezzo + '€</p>' +
                        '<p>Categoria: ' + product.tipo + '</p>' +
                        '<img src="img/' + product.immagine + '" alt="' + product.nome + '" />' +
                        '<a href="GestioneCart?action=add&id=' + product.id + '" class="addToCart">Add to Cart</a>' +
                        '<a href="DettagliS?id=' + product.id + '" class="viewDetails">Dettagli</a>' +
                        '<a href="Preferiti?action=add&id=' + product.id + '" class="addToFavorites">&#10084;</a>' +
                        '</div>'
                    );
                });
            }

            // Gestisce la ricerca per categoria e prezzo
            $('#filterForm').submit(function(event) {
                event.preventDefault();
                var filters = {
                    action: 'filter',
                    prezzo_da: $('#prezzo_da').val(),
                    prezzo_a: $('#prezzo_a').val(),
                    categoria: $('#filterCategoria').val()
                };
                $.ajax({
                    url: 'GestioneCatalogo',
                    method: 'GET',
                    data: filters,
                    success: function(response) {
                        displayProducts(response);
                    },
                    error: function() {
                        alert("Errore nell'applicazione dei filtri");
                    }
                });
            });
        });
    </script>
</head>
<body>
<h1>Catalogo Prodotti</h1>

<div class="grid-container">
    <div>
        <h2>Ricerca per Categoria e Prezzo</h2>
        <form id="filterForm">
            <input type="text" id="prezzo_da" name="prezzo_da" placeholder="Prezzo da" class="filterInput">
            <input type="text" id="prezzo_a" name="prezzo_a" placeholder="Prezzo a" class="filterInput">
            <input type="text" id="filterCategoria" name="categoria" placeholder="Categoria" class="filterInput">
            <button type="submit" class="filterButton">Applica Filtri</button>
        </form>
    </div>

    <div id="productContainer" class="catalogo">
        <!-- I prodotti verranno visualizzati qui -->
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
