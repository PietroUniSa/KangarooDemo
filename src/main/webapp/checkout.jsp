<%@include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.AddressBean" %>
<%@ page import="ita.kangaroo.model.MetodoPagamentoBean" %>
<%@ page import="java.util.ArrayList" %>

<%
    ArrayList<AddressBean> addresses = (ArrayList<AddressBean>) request.getAttribute("addresses");
    ArrayList<MetodoPagamentoBean> payments = (ArrayList<MetodoPagamentoBean>) request.getAttribute("payments");

    String error = (String) request.getAttribute("error");
%>
    <meta content="width=device-width, initial-scale=1" name="viewport" />
    <meta charset="UTF-8">
    <title>Checkout</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="styles/formStyle.css" type="text/css">

    <style>
        .grid-container {
            display: grid;
            grid-template-columns: auto ;
            grid-template-rows: auto auto auto;
            gap: 60px;
            background: rgba(250,250,250);
        }

        .grid-container > div {
            font-size: 30px;
        }


        .container{
            display: grid;
            grid-template-columns: 15% 70% 15%;
            width: 100%;
        }

        .transparentCard{
            grid-column:  2/3;
            display: grid;
            grid-template-columns: 1fr;
            grid-template-rows: 10% 70% 20%;
            height:100%;
            width:100%;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            gap: 20px;

        }


        .title{

            grid-column: 2;
            grid-row: 1;
            width:100%;
            margin: auto;
            bottom: 30px;
            text-align:center;
        }

        .submitContainer{
            text-align:center;
            grid-row: 3;
        }

        .submit{
            height: 200%;
            width: 400%;
            padding:10%;
            margin: auto;
        }

        .inputBox{
            width: 55%;
            display: inline-flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
        }

        .special{
            width: 55%;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: row;
        }

        .inputBox input[type="radio"]{
            border: 0px;
            width: 100%;
            height: 1em;
        }

        .inputBox input[type="checkbox"]{
            border: 0px;
            width: 100%;
            height: 2em;
        }


        .error{
            transform: translateY(-100%);
        }

        @media only screen and (max-width: 900px){
            .container{
                grid-template-columns: 1fr 5fr 1fr;
            }
            .transparentCard{
                width:100%;
            }
        }

        @media only screen and (max-width: 480px){
            .container{
                grid-template-columns: 1fr;
            }

            .transparentCard{
                grid-column:1;
                grid-template-columns: 1fr;
                grid-template-rows: 1fr 3fr 1fr;
                width:100%;
            }

            .title{
                grid-column:1;
                grid-row: 1;
            }

            .submitContainer{
                grid-column: 1;
                grid-row: 3;
            }

        }

    </style>

</head>
<body>

<div class="grid-container">
    <form action="GestionePagamento" method="post" onsubmit="event.preventDefault(); validate(this)">
        <div class="container">
            <div class="transparentCard">
                <span class="title">Pagamento</span>

                <% if (error != null){%>
                <div class="errorNoTranslate"><%=error%></div>
                <%}%>

                <input type="hidden" name="action" value="confirm_buy">

                <div class="inputBoxx">
                    <span class="error" id="errorDestinatario"></span>
                    <input id="destinatario" name="destinatario" type="text" maxlenght="30" required autocomplete="off" placeholder="destinatario" >
                    <label id="destinatarioLabel" for="destinatario">Insert addressee:</label>
                </div>

                <div class="inputBoxx">
                    <input id="note" name="note" type="text" maxlength="100" autocomplete="off" placeholder="Insert notes...">
                    <label for="note">Notes:</label>
                </div>


                <div class="inputBoxx special">
                    <label for="spedizione">Spedizione:</label>
                    <input type="hidden" name="action" value="spedizione">
                    <input type="radio" id ="1"  name="spedizione" value="Standard" required>
                    <label for="1">Spedizione Standard (7 - 10  Giorni lavorativi)</label>
                    <input type="radio" id="2" name="spedizione" value="Economic"  required>
                    <label for="2">Spedizione Economica(12 - 15  Giorni lavorativi) </label>
                    <input type="radio" id="3" name="spedizione" value="Express" required>
                    <label for="3">Spedizione Express(3 - 5  Giorni lavorativi) ( <b>&euro;</b> )</label>
                </div>



                <div class="inputBoxx special">
                    <input type="hidden" name="action" value="metodo_di_pagamento">
                    <input type="radio" id="credit_card" name="metodo_di_pagamento" value="carta_di_credito" required onchange="enableCard()">
                    <label for="credit_card">Credit card</label>
                    <input type="radio" id="charge_card" name="metodo_di_pagamento" value="carta_di_debito" required onchange="enableCard()">
                    <label for="charge_card">Charge card</label>
                    <input type="radio" id="paypal" name="metodo_di_pagamento" value="Paypal" required onchange="enableCard()">
                    <label for="paypal">Paypal</label>
                </div>




                <div class="inputBoxx special">
                    <label for="carta">Insrisci carta:</label>
                    <select name="carta" id="carta" disabled >

                        <%  for(int i=0; i<payments.size(); i++) { %>
                        <option value="<%= payments.get(i).getId()%>"><%="****" + payments.get(i).getNumero_carta().substring(payments.get(i).getNumero_carta().length()-4) %></option>
                        <% } %>

                    </select>
                </div>

                <div class="inputBoxx special">
                    <label for="indirizzo">Inserisci indirizzo spedizione:</label><br>
                    <select name="indirizzo" id="indirizzo" >

                        <%  for(int i=0; i<addresses.size(); i++) { %>
                        <option value="<%= addresses.get(i).getId()%>"><%=addresses.get(i).getVia() + addresses.get(i).getCitta() %></option>
                        <% } %>

                    </select>
                </div>

                <div class="submitContainer">
                    <input class="submit" type="submit" value="Pay">
                </div>
            </div>
        </div>
    </form>


<script>
    $(document).ready(function () {

        $("#destinatario").on("input", function() {
            if ($("#destinatario").val().length !== 0)
                $("#destinatarioLabel").addClass("notEmpty");
            else
                $("#destinatarioLabel").removeClass("notEmpty");
        });

    })

    function validate(obj) {
        let valid = true;

        let addressee = document.getElementsByName("destinatario")[0];
        let errorDestinatario = document.getElementById('errorDestinatario');
        if(!checkLettere(addressee)) {
            valid = false;
            errorDestinatario.textContent = "Error: addressee must contain only alphabetic characters";
        } else {
            errorDestinatario.textContent = "";
        }

        if(valid) obj.submit();
    }


    function checkLettere(inputtxt) {
        let addressee = /^[A-Za-z ]+$/;
        if(inputtxt.value.match(addressee))
            return true

        return false;
    }

    function enableCard() {
        var radio1 = document.getElementById('credit_card');
        var radio2 = document.getElementById('charge_card');
        var radio3 = document.getElementById('paypal');
        var carta = document.getElementById('carta');

        if (radio1.value !== '' || radio2.value !== '' || radio3.value !== '' ) {
            carta.disabled = false;
        }
    }

</script>
<%@include file="footer.jsp" %>
</body>
</html>