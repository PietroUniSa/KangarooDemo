<%@include file="header.jsp" %>
<%@ page import="ita.kangaroo.model.AddressBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ita.kangaroo.model.MetodoPagamentoBean" %>
<%@ page import="ita.kangaroo.model.utenteBean" %>

<%
    // Recupero degli oggetti necessari dalle richieste e dalla sessione
    ArrayList<AddressBean> addresses = (ArrayList<AddressBean>) request.getAttribute("addresses");
    ArrayList<MetodoPagamentoBean> payments = (ArrayList<MetodoPagamentoBean>) request.getAttribute("payments");
    utenteBean client = (utenteBean) request.getSession().getAttribute("utente");
    ArrayList<utenteBean> clients = (ArrayList<utenteBean>) request.getAttribute("clienti");

    // Gestione della logica per inoltrare la richiesta a un'altra servlet
    if (clients == null && client != null && client.getEmail().equals("provoloni@example.com")) {
        request.getRequestDispatcher("/UtenteServlet").forward(request, response);
        return;
    }

    // Recupero di eventuali messaggi di errore
    String error = (String) request.getAttribute("error");
    String carterror = (String) request.getAttribute("carterror");
    String clientError = (String) request.getAttribute("clientError");
%>

<title>User page</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<style>

    .error {
        transform: translateY(-300%);
    }

    .searchbar input[type="text"] {
        border-radius: 10px;
        padding: 2px;
        width: 30%;
        height: auto;
        border-top: solid;
        border-bottom: solid;

    }


    @media screen and (max-width: 700px) {

        .numberRow {
            display: none;
        }

        img {

            width: 70px;
            height: 70px;

        }

        table, th, td {
            border: none;
            font-size: 8px;
            padding: 1%;

        }


        table th:first-child {
            border-radius: 10px 0 0 10px;
        }

        table th:last-child {
            border-radius: 0 10px 10px 0;
        }

        input[type="submit"] {
            background-color: #18020C;
            color: #fff;
            border: none;
            padding: 2%;
            margin-top: 2px;
            cursor: pointer;
            border-radius: 20px;
            font-size: 5px;
        }

        input[type="submit"]:hover {
            background-color: #ff9229;
            color: #fff;
            border: none;
            padding: 2%;
            margin-top: 2px;
            cursor: pointer;
            font-size: 5px;
        }

        input[type="number"] {

            width: 15px;
        }

        #reset {

            background-color: #18020C;
            color: #fff;
            border: none;
            padding: 2%;
            margin-top: 2px;
            cursor: pointer;
            border-radius: 20px;
            font-size: 5px;

        }

        #reset:hover {

            background-color: #ff9229;
            color: #fff;
            border: none;
            padding: 2%;
            margin-top: 2px;
            cursor: pointer;
            font-size: 5px;

        }
    }
</style>
</head>
<body>
<div class="grid-container">

    <!-- -------------------- ADMIN --------------------- -->

    <%
        if (client.getEmail().equalsIgnoreCase("provoloni@example.com")) { %>
    <link rel="stylesheet" type="text/css" href="styles/tableStyle.css">

    <form action="AdminServlet" method="get">
        <input type="hidden" name="action" value="clientsNoFilter">
        <h1> Clienti KangarooBuy </h1>
    </form>

    <form action="AdminServlet?action=ByClient" method="post">
        <% if (clientError != null) {%>
        <span class="errorNoTranslate"><%=clientError%></span>
        <%}%>
        <label for="cliente">Inserisci Username Cliente</label>
        <div class="searchbar">
            <input id="cliente" name="cliente" type="text" placeholder="user">
        </div>
        <input type="submit" value="Order">
        <a href="AdminServlet?action=clientsNoFilter" id="reset">Reset</a>
    </form>

    <table>
        <tr>
            <th>Nome</th>
            <th>Cognome</th>
            <th>Username</th>
            <th class="numberRow">Cellulare</th>
            <th>Email</th>
        </tr>
        <% for (utenteBean cliente : clients) { %>
        <tr>
            <td><%=cliente.getNome() %>
            </td>
            <td><%=cliente.getCognome() %>
            </td>
            <td><%=cliente.getUsername() %>
            </td>
            <td class="numberRow"><%=cliente.getTelefono() %>
            </td>
            <td><%=cliente.getEmail() %>
            </td>
        </tr>
        <% } %>
    </table>
    <% } else { %>

    <!-- -------------------- UTENTE -------------------- -->
    <link rel="stylesheet" type="text/css" href="styles/multi-formStyle.css">
    <div class="screen-container">
        <span class="title">Profilo Utente</span>
        <div class="transparentCard">
            <% if (carterror != null) {%>
            <div class="errorNoTranslate"><%=carterror%>
            </div>
            <%}%>
            <% if (error != null) {%>
            <div class="errorNoTranslate"><%=error%>
            </div>
            <%}%>
            <form class="formContainer" action="UtenteServlet?action=modify" method="post"
                  onsubmit="event.preventDefault(); validate(this)">
                <span class="subtitle">Modifica il tuo account</span>

                <div class="inputBox">
                    <span id="errorEmail" class="error"></span>
                    <input id="email" name="email" type="email" maxlength="30" required value="<%=client.getEmail() %>">
                    <label id="emailLabel" for="email">Email:</label>
                </div>

                <div class="inputBox">
                    <span id="errorNome" class="error"></span>
                    <input id="nome" name="nome" type="text" maxlength="30" required value="<%=client.getNome() %>">
                    <label id="nomeLabel" for="nome">Nome:</label>
                </div>

                <div class="inputBox">
                    <span id="errorCognome" class="error"></span>
                    <input id="cognome" name="cognome" type="text" maxlength="30" required
                           value="<%=client.getCognome() %>">
                    <label id="cognomeLabel" for="cognome">Cognome:</label>
                </div>


                <div class="inputBox">
                    <span id="errorIndirizzo" class="error"></span>
                    <input id="indirizzo" name="indirizzo" type="text" maxlength="50" required
                           value="<%=client.getVia() %>">
                    <label id="indirizzoLabel" for="indirizzo">Indirizzo:</label>
                </div>

                <div class="inputBox">
                    <span id="errorCitta" class="error"></span>
                    <input id="citta" name="citta" type="text" maxlength="40" required value="<%=client.getCitta() %>">
                    <label id="cittaLabel" for="citta">Citta:</label>
                </div>

                <div class="inputBox">
                    <span id="errorProvincia" class="error"></span>
                    <input id="provincia" name="provincia" type="text" maxlength="40" required
                           value="<%=client.getProvincia() %>">
                    <label id="provinciaLabel" for="provincia">Provincia:</label>
                </div>

                <div class="inputBox">
                    <span id="errorCap" class="error"></span>
                    <input id="cap" name="cap" type="text" maxlength="5" required value="<%=client.getCap() %>">
                    <label id="capLabel" for="cap">CAP:</label>
                </div>

                <div class="inputBox">
                    <span id="errorTelefono" class="error"></span>
                    <input id="telefono" name="telefono" type="text" maxlength="12" required
                           value="<%=client.getTelefono() %>">
                    <label id="telefonoLabel" for="telefono">Cellulare:</label>
                </div>

                <div class="submitContainer">
                    <input class="submit" type="submit" value="Submit">
                </div>
            </form>
            <!-- Gestione delle carte di pagamento -->
            <% if (payments != null) { %>
            <span class="subtitle">Elimina la tua carta</span>
            <% for (int i = 0; i < payments.size(); i++) { %>
            <form class="formContainer delete" action="UtenteServlet?action=deletePaymentCard" method="post">
                <label for="payment"><%="****" + payments.get(i).getNumero_carta().substring(payments.get(i).getNumero_carta().length() - 4) %>
                </label>
                <input type="hidden" name="id_carta" value="<%= payments.get(i).getId()%>">
                <button value="Delete this card" id="payment"><i class="fa fa-trash" aria-hidden="true"></i></button>
            </form>
            <% } %>
            <% } %>
            <form class="formContainer" action="UtenteServlet?action=addPaymentCard" method="post"
                  onsubmit="event.preventDefault(); validate(this)">
                <span class="subtitle">Aggiungi la tua carta</span>
                <div class="inputBox">
                    <span id="errorNumero_carta" class="error"></span>
                    <input id="numero_carta" name="numero_carta" type="text" maxlength="19" required
                           placeholder="enter card number">
                    <label id="numero_cartaLabel" for="numero_carta">Numero Carta:</label>
                </div>

                <div class="inputBox">
                    <span id="errorCvv" class="error"></span>
                    <input id="cvv" name="cvv" type="text" maxlength="3" required placeholder="123">
                    <label id="cvvLabel" for="cvv">Cvv:</label>
                </div>

                <div class="inputBox special">
                    <label for="data_scadenza">Data Scadenza:</label>
                    <input id="data_scadenza" name="data_scadenza" type="text" maxlength="10" required
                           placeholder="AAAA-MM-GG">
                    <span id="errorData_scadenza" class="error"></span>
                </div>

                <div class="inputBox special">
                    <label for="circuito">Circuito Carta:</label>
                    <select name="circuito" id="circuito">
                        <option value="PayPal"> Paypal</option>
                        <option value="Mastercard">MasterCard</option>
                        <option value="VISA">VISA</option>
                        <option value="PostePay">Postepay</option>
                        <option value="American Express">American Express</option>
                        <option value="Bancomat">Bancomat</option>
                    </select>
                </div>

                <div class="submitContainer">
                    <input class="submit" type="submit" value="Add">
                </div>
            </form>
            <!-- Gestione degli indirizzi -->
            <% if (addresses != null) { %>
            <span class="subtitle">Elimina il tuo indirizzo</span>
            <% for (int i = 0; i < addresses.size(); i++) { %>
            <form class="formContainer delete" action="UtenteServlet?action=deleteAddress" method="post">
                <label for="address"><%=addresses.get(i).getVia() + " " + addresses.get(i).getCitta() %>
                </label>
                <input type="hidden" name="id_indirizzo" value="<%= addresses.get(i).getId()%>">
                <button value="Delete this address" id="address"><i class="fa fa-trash" aria-hidden="true"></i></button>
            </form>
            <% } %>
            <% } %>
            <!-- Modulo per aggiungere un nuovo indirizzo -->
            <form class="formContainer" action="UtenteServlet?action=addAddress" method="post"
                  onsubmit="event.preventDefault(); validate_address(this)">
                <span class="subtitle">Aggiungi/modifica il tuo indirizzo</span>
                <div class="inputBox">
                    <span id="errorVia_indirizzo" class="error"></span>
                    <input id="via_indirizzo" name="via_indirizzo" type="text" maxlength="50" required
                           placeholder="Enter Street">
                    <label id="via_indirizzoLabel" for="via_indirizzo">Via:</label>
                </div>

                <div class="inputBox">
                    <span id="errorCitta_indirizzo" class="error"></span>
                    <input id="citta_indirizzo" name="citta_indirizzo" type="text" maxlength="50" required
                           placeholder="London">
                    <label id="citta_indirizzoLabel" for="citta_indirizzo">Citta':</label>
                </div>

                <div class="inputBox">
                    <span id="errorCAP_indirizzo" class="error"></span>
                    <input id="CAP_indirizzo" name="CAP_indirizzo" type="text" maxlength="50" required
                           placeholder="00000">
                    <label id="CAP_indirizzoLabel" for="CAP_indirizzo">CAP:</label>
                </div>

                <div class="submitContainer">
                    <input class="submit" type="submit" value="Add">
                </div>

            </form>
        </div>
    </div>
    <% } %>

</div>
<script>
    $(document).ready(function () {

        $("#email").on("input", function () {
            if ($("#email").val().length !== 0)
                $("#emailLabel").addClass("notEmpty");
            else
                $("#emailLabel").removeClass("notEmpty");
        });

        $("#cap").on("input", function () {
            if ($("#cap").val().length !== 0)
                $("#capLabel").addClass("notEmpty");
            else
                $("#capLabel").removeClass("notEmpty");
        });

        $("#nome").on("input", function () {
            if ($("#nome").val().length !== 0)
                $("#nomeLabel").addClass("notEmpty");
            else
                $("#nomeLabel").removeClass("notEmpty");
        });

        $("#cognome").on("input", function () {
            if ($("#cognome").val().length !== 0)
                $("#cognomeLabel").addClass("notEmpty");
            else
                $("#cognomeLabel").removeClass("notEmpty");
        });


        $("#indirizzo").on("input", function () {
            if ($("#indirizzo").val().length !== 0)
                $("#indirizzoLabel").addClass("notEmpty");
            else
                $("#indirizzoLabel").removeClass("notEmpty");
        });

        $("#citta").on("input", function () {
            if ($("#citta").val().length !== 0)
                $("#cittaLabel").addClass("notEmpty");
            else
                $("#cittaLabel").removeClass("notEmpty");
        });

        $("#provincia").on("input", function () {
            if ($("#provincia").val().length !== 0)
                $("#provinciaLabel").addClass("notEmpty");
            else
                $("#provinciaLabel").removeClass("notEmpty");
        });

        $("#telefono").on("input", function () {
            if ($("#telefono").val().length !== 0)
                $("#telefonoLabel").addClass("notEmpty");
            else
                $("#telefonoLabel").removeClass("notEmpty");
        });

        $("#via_indirizzo").on("input", function () {
            if ($("#via_indirizzo").val().length !== 0)
                $("#via_indirizzoLabel").addClass("notEmpty");
            else
                $("#via_indirizzoLabel").removeClass("notEmpty");
        });

        $("#citta_indirizzo").on("input", function () {
            if ($("#citta_indirizzo").val().length !== 0)
                $("#citta_indirizzoLabel").addClass("notEmpty");
            else
                $("#citta_indirizzoLabel").removeClass("notEmpty");
        });

        $("#CAP_indirizzo").on("input", function () {
            if ($("#CAP_indirizzo").val().length !== 0)
                $("#CAP_indirizzoLabel").addClass("notEmpty");
            else
                $("#CAP_indirizzoLabel").removeClass("notEmpty");
        });

        $("#cvv").on("input", function () {
            if ($("#cvv").val().length !== 0)
                $("#cvvLabel").addClass("notEmpty");
            else
                $("#cvvLabel").removeClass("notEmpty");
        });

        $("#numero_carta").on("input", function () {
            if ($("#numero_carta").val().length !== 0)
                $("#numero_cartaLabel").addClass("notEmpty");
            else
                $("#numero_cartaLabel").removeClass("notEmpty");
        });

    })


    function validate(obj) {
        let valid = true;

        let email = document.getElementsByName("email")[0];
        let errorEmail = document.getElementById('errorEmail');
        if (!checkEmail(email)) {
            valid = false;
            errorEmail.textContent = "Errore: indirizzo email invalido";
        } else {
            errorEmail.textContent = "";
        }

        let name = document.getElementsByName("nome")[0];
        let errorNome = document.getElementById('errorNome');
        if (!checkLettere(name)) {
            valid = false;
            errorNome.textContent = "Errore: solo caratteri alfabetici consentiti nel nome";
        } else {
            errorNome.textContent = "";
        }

        let surname = document.getElementsByName("cognome")[0];
        let errorCognome = document.getElementById('errorCognome');
        if (!checkLettere(surname)) {
            valid = false;
            errorCognome.textContent = "Errore: solo caratteri alfabetici consentiti nel cognome";
        } else {
            errorCognome.textContent = "";
        }

        let indirizzo = document.getElementsByName("indirizzo")[0];
        let errorIndirizzo = document.getElementById('errorIndirizzo');
        if (!checkIndirizzo(indirizzo)) {
            valid = false;
            errorIndirizzo.textContent = "Errore: formato indirizzo invalido";
        } else {
            errorIndirizzo.textContent = "";
        }

        let citta = document.getElementsByName("citta")[0];
        let errorCitta = document.getElementById('errorCitta');
        if (!checkLettere(citta)) {
            valid = false;
            errorCitta.textContent = "Errore: solo caratteri alfabetici consentiti per citta";
        } else {
            errorCitta.textContent = "";
        }

        let provincia = document.getElementsByName("provincia")[0];
        let errorProvincia = document.getElementById('errorProvincia');
        if (!checkLettere(provincia)) {
            valid = false;
            errorProvincia.textContent = "Errore: solo caratteri alfabetici consentiti per provincia";
        } else {
            errorProvincia.textContent = "";
        }

        let cap = document.getElementsByName("cap")[0];
        let errorCap = document.getElementById('errorCap');
        if (!checkCap(cap)) {
            valid = false;
            errorCap.textContent = "Errore: CAP deve contenere 5 numeri";
        } else {
            errorCap.textContent = "";
        }

        let telefono = document.getElementsByName("telefono")[0];
        let errorTelefono = document.getElementById('errorTelefono');
        if (!checkTelefono(telefono)) {
            valid = false;
            errorTelefono.textContent = "Errore: Formato numero cellulare non valido";
        } else {
            errorTelefono.textContent = "";
        }

        if (valid) obj.submit();
    }

    function validate_payment(obj) {
        let valid = true;

        let numero_carta = document.getElementsByName("numero_carta")[0];
        let errorNumero_carta = document.getElementById('errorNumero_carta');
        if (!checkNumero_carta(numero_carta.value)) {
            valid = false;
            errorNumero_carta.textContent = "Errore: il numero della carta deve essere compreso tra le 13 e le 19 cifre";
        } else {
            errorNumero_carta.textContent = "";
        }

        let cvv = document.getElementsByName("cvv")[0];
        let errorCvv = document.getElementById('errorCvv');
        if (!checkCvv(cvv.value)) {
            valid = false;
            errorCvv.textContent = "Errorr: Cvv deve contenere 3 cifre";
        } else {
            errorCvv.textContent = "";
        }

        let data_scadenza = document.getElementById('data_scadenza');
        let errorData_scadenza = document.getElementById('errorData_scadenza');
        if (!checkData(data_scadenza.value)) {
            valid = false;
            errorData_scadenza.textContent = "Errore: il formato della data deve risultare AAAA-MM-GG";
        } else {
            errorData_scadenza.textContent = "";
        }

        if (valid) obj.submit();
    }

    function validate_address(obj) {

        let valid = true;
        let indirizzo = document.getElementsByName("via_indirizzo")[0];
        let errorVia_indirizzo = document.getElementById('errorVia_indirizzo');
        if (!checkIndirizzo(indirizzo)) {
            valid = false;
            errorVia_indirizzo.textContent = "Errore: formato indirizzo invalido";
        } else {
            errorVia_indirizzo.textContent = "";
        }

        let citta = document.getElementsByName("citta_indirizzo")[0];
        let errorCitta_indirizzo = document.getElementById('errorCitta_indirizzo');
        if (!checkLettere(citta)) {
            valid = false;
            errorCitta_indirizzo.textContent = "Errore: solo caratteri alfabetici consentiti per citta";
        } else {
            errorCitta_indirizzo.textContent = "";
        }
        let cap = document.getElementsByName("CAP_indirizzo")[0];
        let errorCAP_indirizzo = document.getElementById('errorCAP_indirizzo');
        if (!checkCap(cap)) {
            valid = false;
            errorCAP_indirizzo.textContent = "Errore: CAP deve contenere 5 numeri";
        } else {
            errorCAP_indirizzo.textContent = "";
        }

        if (valid) obj.submit();

    }

    function checkEmail(inputtxt) {
        let email = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if (inputtxt.value.match(email))
            return true;

        return false;
    }

    function checkPsw(inputtxt) {
        //almeno una lettera, almeno una cifra, da 6 a 20 caratteri
        let psw = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/;
        if (inputtxt.value.match(psw))
            return true;

        return false;
    }

    function checkUsername(inputtxt) {
        let username = /^[a-zA-Z0-9_-]{6,20}$/;
        if (inputtxt.value.match(username))
            return true;

        return false;
    }

    function checkLettere(inputtxt) {
        let name = /^[A-Za-z ]+$/;
        if (inputtxt.value.match(name))
            return true

        return false;
    }


    function checkIndirizzo(inputtxt) {
        let indirizzo = /^([A-Za-z]+\s)+\d+$/;
        if (inputtxt.value.match(indirizzo))
            return true

        return false;
    }

    function checkCap(inputtxt) {
        let cap = /^\d{5}$/;
        if (inputtxt.value.match(cap))
            return true

        return false;
    }

    function checkTelefono(inputtxt) {
        let telefono = /^\d{12}$/;
        if (inputtxt.value.match(telefono))
            return true;

        return false;
    }

    function checkNumero_carta(inputtxt) {
        let numero_carta = /^\d{13,19}$/;
        if (inputtxt.value.match(numero_carta))
            return true;

        return false;
    }

    function checkCvv(inputtxt) {
        let cvv = /^\d{3}$/;
        if (inputtxt.value.match(cvv))
            return true;

        return false;
    }

    function checkData(inputtxt) {
        // Espressione regolare per il formato AAAA-MM-GG
        let dateRegex = /^\d{4}\-\d{2}\-\d{2}$/;

        // Verifica se l'input corrisponde alla regex
        if (inputtxt.match(dateRegex)) {
            // Verifica ulteriore: controllo sulla validità della data con JavaScript
            let parts = inputtxt.split('-');
            let year = parseInt(parts[0], 10);
            let month = parseInt(parts[1], 10);
            let day = parseInt(parts[2], 10);

            // Controllo su anno valido tra 1000 e 9999
            if (year >= 1000 && year <= 9999) {
                // Controllo su mese valido tra 01 e 12
                if (month >= 1 && month <= 12) {
                    // Controllo su giorno valido (considerando il mese e l'anno specifico)
                    let daysInMonth = new Date(year, month, 0).getDate();
                    if (day >= 1 && day <= daysInMonth) {
                        return true; // La data è valida
                    }
                }
            }
        }
        return false; // La data non è nel formato o non è valida
    }


</script>
<%@include file="footer.jsp" %>
</body>
</html>