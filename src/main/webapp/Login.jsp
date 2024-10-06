<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="styles/login.css">
    <title>Login page</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
   <link rel="stylesheet" type="text/css" href="styles/alertStyle.css">
</head>
<body>
<div class="login-box">
    <div class="login-header">
        <header>Login</header>
    </div>
    <form action="LoginServlet" method="post" onsubmit="event.preventDefault(); validate(this)">
        <div class="input-box">
            <span class="error" id="errorEmail"></span>
            <input id="email" name="email" type="email" maxlength="30" required placeholder="myemail@domain.com" autocomplete="off" class="input-field">
        </div>
        <div class="input-box">
            <input id="password" name="password" type="password" maxlength="20" required placeholder="password123" autocomplete="off" class="input-field">
        </div>
        <input type="hidden" name="action" value="login">
        <div class="input-submit">
            <input class="submit-btn" type="submit" value="Submit">
        </div>
        <div class="link">
            <span>Non sei registrato? Iscriviti <a href="RegistrationServlet"> qui!</a></span>
            <span>Torna alla <a href="HomeServlet"> home </a></span>
        </div>
    </form>
</div>
<div id="custom-alert" class="custom-alert">
    <p>Compilare tutti i campi.</p>
    <button onclick="closeAlert()">Chiudi</button>
</div>
<script>
    $(document).ready(function () {
        $("#email").on("input", function () {
            if ($("#email").val().length !== 0)
                $(this).addClass("notEmpty");
            else
                $(this).removeClass("notEmpty");
        });
    });

    function validate(obj) {
        let valid = true;
        let email = document.getElementById("email");
        let password = document.getElementById("password");
        let errorEmail = document.getElementById('errorEmail');

        if (email.value.trim() === "" || password.value.trim() === "") {
            valid = false;
            showAlert("Compilare tutti i campi.");
        } else if (!checkEmail(email)) {
            valid = false;
            errorEmail.textContent = "Error: invalid email address";
        } else {
            errorEmail.textContent = "";
        }

        if (valid) obj.submit();
    }

    function checkEmail(inputtxt) {
        let email = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        return inputtxt.value.match(email) ? true : false;
    }

    function showAlert(message) {
        let alertBox = document.getElementById("custom-alert");
        alertBox.querySelector("p").textContent = message;
        alertBox.style.display = "block";
    }

    function closeAlert() {
        document.getElementById("custom-alert").style.display = "none";
    }
</script>
</body>
</html>
