<!DOCTYPE html>
<html lang="en">
<%
    String error = (String) request.getAttribute("error");
%>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="./styles/formStyle.css">
    <title>Registration page</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <style>
        *{
            height: 100%;
            margin: auto;
            width: 100%;
        }

        .container{
            display: grid;
            grid-template-columns: 25% 50% 25%;
            width: 100%;
        }

        .inputBox input {
            width: 100%;
            height: 60px;
            font-size: 17px;
            padding: 20px 25px;
            margin-bottom: 15px;
            border-radius: 30px;
            border: none;
            box-shadow: 0px 5px 10px 1px rgba(0, 0, 0, 0.05);
            outline: none;
            transition: .3s;
        }
        .title{
            grid-column: 1/3;
            grid-row: 1;
            width:100%;
            height:100%;
            margin-top: 8%;
            bottom: 30px;
            text-align:center;
        }

        .submitContainer{
            text-align:center;
        }

        .submit{
            width: 55%;
            height: 60px;
            background: #FF9229;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            transition: .3s;
            font-size: 120%;
            color: black;
        }

        .link{
            width:100%;
            height:100%;
            text-align:center;
        }

        .inputBox{
            width: 55%;
            display: inline-flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
        }

        .error{
            transform: translateY(-100%);
        }

        @media only screen and (max-width: 900px){ /* media query per tablet e schermi laptop medi */
            .container{
                grid-template-columns: 1fr 5fr 1fr;
            }
            .card{
                width:100%;
            }
        }

        @media only screen and (max-width: 480px){ /* media query per smartphone */
            .container{
                grid-template-columns: 1fr;
            }
            .card{
                grid-column:1;
                grid-template-columns: 1fr;
                grid-template-rows: 1fr 3fr 1fr;
                width:100%;
            }

            .title{
                grid-column:1;
                grid-row: 1;
            }
        }
    </style>
</head>
<body>
<form action="RegistrationServlet" method="post" onsubmit="return submitForm(this)">
    <div class="container">
        <div class="card">
            <span class="title">Register</span>

            <% if (error != null){%>
            <div class="errorNoTranslate"><%=error%></div>
            <%}%>

            <div class="inputBox">
                <span class="error" id="errorEmail"></span>
                <span class="error" id="errorCheckEmail"></span>
                <input id="email" name="email" type="email" maxlenght="30" required autocomplete="off" placeholder="email" onblur="checkEmailAjax()">
            </div>

            <div class="inputBox">
                <span class="error" id="errorPsw"></span>
                <input id="password" name="password" type="password" maxlenght="20" autocomplete="off" required placeholder="password">
            </div>

            <div class="inputBox">
                <span class="error" id="errorUsername"></span>
                <span class="error" id="errorCheckUsername"></span>
                <input id ="username" name="username" type="text" maxlenght="20" required autocomplete="off" placeholder="nickname" onblur="checkUsernameAjax()">
            </div>

            <div class="inputBox">
                <span class="error" id="errorNome"></span>
                <input id="nome" name="nome" type="text" maxlenght="30" required autocomplete="off" placeholder="nome">
            </div>

            <div class="inputBox">
                <span class="error" id="errorCognome"></span>
                <input id="cognome" name="cognome" type="text" maxlenght="30" required autocomplete="off" placeholder="cognome">
            </div>

            <div class="inputBox">
                <span class="error" id="errorIndirizzo"></span>
                <input id="indirizzo" name="indirizzo" type="text" maxlenght="50" autocomplete="off" required placeholder="indirizzo">
            </div>

            <div class="inputBox">
                <span class="error" id="errorCitta"></span>
                <input id="citta" name="citta" type="text" maxlenght="40" required autocomplete="off" placeholder="citta'">
            </div>

            <div class="inputBox">
                <span class="error" id="errorProvincia"></span>
                <input id="provincia" name="provincia" type="text" maxlenght="40" autocomplete="off" required placeholder="provincia">
            </div>

            <div class="inputBox">
                <span class="error" id="errorCap"></span>
                <input id="cap" name="cap" type="text" maxlenght="5" required autocomplete="off" placeholder="cap">
            </div>

            <div class="inputBox">
                <span class="error" id="errorTelefono"></span>
                <input id="telefono" name="telefono" type="text" maxlength="12" required autocomplete="off" placeholder="telefono">
            </div>

            <div class="submitContainer">
                <input type="hidden" name="action" value="register">
                <input class="submit" type="submit" value="Submit">

                <div class="link">
                    <span>Hai gia' un profilo? <a href="LoginServlet">Accedi</a></span>
                    <span>Torna alla <a href="HomeServlet">home</a></span>
                </div>
            </div>
        </div>
    </div>
</form>
<script>
    $(document).ready(function () {
        $("input").on("input", function () {
            if ($(this).val().length !== 0) {
                $(this).prev("label").addClass("notEmpty");
            } else {
                $(this).prev("label").removeClass("notEmpty");
            }
        });

        $("#registrationForm").submit(function (event) {
            event.preventDefault(); // Prevent the default form submission
            if (validate(this)) {
                this.submit(); // If form is valid, submit it
            }
        });
    });

    function submitForm(form) {
        if (validate(form)) {
            form.submit();
        } else {
            return false;
        }
    }

    function validate(form) {
        let valid = true;

        let email = form.email;
        let errorEmail = document.getElementById("errorEmail");
        if (!checkEmail(email)) {
            valid = false;
            errorEmail.textContent = "Error: invalid email address";
        } else {
            errorEmail.textContent = "";
        }

        let password = form.password;
        let errorPsw = document.getElementById("errorPsw");
        if (!checkPassword(password)) {
            valid = false;
            errorPsw.textContent = "Error: password must be at least 6 characters long and contain at least one letter and one number";
        } else {
            errorPsw.textContent = "";
        }

        let username = form.username;
        let errorUsername = document.getElementById("errorUsername");
        if (!checkUsername(username)) {
            valid = false;
            errorUsername.textContent = "Error: username must be between 6 and 20 characters long and can only contain letters, numbers, underscores, and hyphens";
        } else {
            errorUsername.textContent = "";
        }

        let nome = form.nome;
        let errorNome = document.getElementById("errorNome");
        if (!checkLetters(nome)) {
            valid = false;
            errorNome.textContent = "Error: name must contain only alphabetic characters";
        } else {
            errorNome.textContent = "";
        }

        let cognome = form.cognome;
        let errorCognome = document.getElementById("errorCognome");
        if (!checkLetters(cognome)) {
            valid = false;
            errorCognome.textContent = "Error: surname must contain only alphabetic characters";
        } else {
            errorCognome.textContent = "";
        }

        let indirizzo = form.indirizzo;
        let errorIndirizzo = document.getElementById("errorIndirizzo");
        if (!checkIndirizzo(indirizzo)) {
            valid = false;
            errorIndirizzo.textContent = "Error: invalid street format";
        } else {
            errorIndirizzo.textContent = "";
        }

        let citta = form.citta;
        let errorCitta = document.getElementById("errorCitta");
        if (!checkLetters(citta)) {
            valid = false;
            errorCitta.textContent = "Error: city must contain only alphabetic characters";
        } else {
            errorCitta.textContent = "";
        }

        let provincia = form.provincia;
        let errorProvincia = document.getElementById("errorProvincia");
        if (!checkLetters(provincia)) {
            valid = false;
            errorProvincia.textContent = "Error: province must contain only alphabetic characters";
        } else {
            errorProvincia.textContent = "";
        }

        let cap = form.cap;
        let errorCap = document.getElementById("errorCap");
        if (!checkCap(cap)) {
            valid = false;
            errorCap.textContent = "Error: CAP must contain 5 numbers";
        } else {
            errorCap.textContent = "";
        }

        let telefono = form.telefono;
        let errorTelefono = document.getElementById("errorTelefono");
        if (!checkPhone(telefono)) {
            valid = false;
            errorTelefono.textContent = "Error: invalid phone number format";
        } else {
            errorTelefono.textContent = "";
        }

        return valid;
    }

    function checkEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value);
    }

    function checkPassword(password) {
        return /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/.test(password.value);
    }

    function checkUsername(username) {
        return /^[a-zA-Z0-9_-]{6,20}$/.test(username.value);
    }

    function checkLetters(input) {
        return /^[a-zA-Z]+$/.test(input.value);
    }

    function checkIndirizzo(indirizzo) {
        return /^[a-zA-Z0-9\s,'-]*$/.test(indirizzo.value);
    }

    function checkCap(cap) {
        return /^\d{5}$/.test(cap.value);
    }

    function checkPhone(phone) {
        return /^\d{12}$/.test(phone.value);
    }

    function checkEmailAjax() {
        let email = document.getElementById("email").value;
        $.ajax({
            type: 'POST',
            url: 'CheckEmail',
            data: {email: email},
            success: function (response) {
                let errorCheckEmail = document.getElementById("errorCheckEmail");
                if (response === "error") {
                    errorCheckEmail.textContent = "Error: email already in use";
                } else {
                    errorCheckEmail.textContent = "";
                }
            }
        });
    }

    function checkUsernameAjax() {
        let username = document.getElementById("username").value;
        $.ajax({
            type: 'POST',
            url: 'CheckUsername',
            data: {username: username},
            success: function (response) {
                let errorCheckUsername = document.getElementById("errorCheckUsername");
                if (response === "error") {
                    errorCheckUsername.textContent = "Error: username already in use";
                } else {
                    errorCheckUsername.textContent = "";
                }
            }
        });
    }
</script>

</body>
</html>
