<%@ include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.OrderProductBean" %>
<%@ page import="ita.kangaroo.model.ProdottoBean" %>
<%@ page import="ita.kangaroo.model.FatturazioneBean" %>
<%@ page import="java.util.ArrayList" %>
<%
    ArrayList<OrderProductBean> products = (ArrayList<OrderProductBean>)request.getAttribute("orderProducts");
    ArrayList<ProdottoBean> prodotti = (ArrayList<ProdottoBean>)request.getAttribute("products");
    FatturazioneBean fattura = (FatturazioneBean) request.getAttribute("invoice");
%>
<title>Invoice</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            text-align: center;
        }
        h1 {
            margin-bottom: 20px;
        }
        .table-container {
            margin: 0 auto;
            width: 80%;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            border-top: 3px solid #000;
            margin: 0 auto;
        }

        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        .item-name {
            width: 60%;
        }

        .item-price, .item-quantity, .item-total {
            text-align: right;
        }

        .total-row {
            font-weight: bold;
        }
        .invoice-total {
            font-size: 1.2em;
            text-align: right;
        }
        .invoice-details {
            text-align: right;
        }
        .divider {
            width: 100%;
            border-top: 1px solid #ddd;
            margin: 20px 0;
        }
        #stampButton {
            background-color: #333;
            color: #fff;
            border: none;
            padding: 10px 20px;
        }
        #stampButton:hover {
            background-color: #ff9229;
            border-radius: 14px;
        }
    </style>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.3/jspdf.umd.min.js"></script>
</head>
<body>
<h1>Fattura</h1>

<button id="stampButton" onclick="eseguiComandoRapido()">Download</button><br>
<div class="table-container">
    <table>
        <thead>
        <tr>
            <th class="item-name">Prodotto</th>
            <th class="item-price">Prezzo</th>
            <th class="item-quantity">Quantita</th>
            <th class="item-total">Totale</th>
        </tr>
        </thead>
        <tbody>
        <% for(int i=0 ;i< products.size();i++){
        %>
        <tr>
            <td class="item-name"><%=prodotti.get(i).getNome()%></td>
            <td class="item-price"><%=products.get(i).getPrezzo()%>&euro;</td>
            <td class="item-quantity"><%=products.get(i).getQuantita()%></td>
            <td class="item-total"><%=products.get(i).getPrezzo() * products.get(i).getQuantita()%></td>
        </tr>

        <% } %>

        </tbody>
        <tfoot>
        <tr class="total-row">
            <td colspan="3">IVA</td>
            <td class="invoice-total"><%= fattura.getIva() %>&euro;</td>
        </tr>
        <tr class="total-row">
            <td colspan="3">Totale</td>
            <td class="invoice-total"><%= fattura.getImporto()%>&euro;</td>
        </tr>
        </tfoot>
    </table>
</div>
<div class="table-container">
    <table>
        <tbody>
        <tr>
            <td><strong>Dettagli Fattura</strong></td>
        </tr>
        <tr>
            <td>SDI (Supplier Delivery Information)</td>
            <td class="invoice-details"><%= fattura.getSdi() %></td>
        </tr>
        <tr>
            <td>Data Emissione</td>
            <td class="invoice-details"><%= fattura.getData_emissione() %></td>
        </tr>
        <tr>
            <td>Data Scadenza</td>
            <td class="invoice-details"><%= fattura.getData_scadenza() %></td>
        </tr>
        <tr>
            <td>Stato Pagamento</td>
            <td class="invoice-details"><%= fattura.getStato_pagamento() %></td>
        </tr>
        </tbody>
    </table>
</div>
<div class="divider"></div>

<script>
    function handleKeyDown(event) {
        if (event.ctrlKey && event.key === 'p') {
            eseguiComandoRapido();
        }
    }
    function eseguiComandoRapido() {
        let stampButton = document.getElementById('stampButton');
        stampButton.classList.add('hidden');
        document.getElementById("stampButton").style.visibility = "hidden";
        window.print();
        document.getElementById("stampButton").style.visibility = "initial";
    }
</script>
<%@ include file="footer.jsp" %>
</body>
</html>