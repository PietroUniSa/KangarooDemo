<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Error</title>
    <link rel="stylesheet" type="text/css" href="../styles/errorStyle.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
    <h3>Ops... You don't have access to this page!</h3>

        <img src="images//error.png" class="errorImg" alt="Error">
    
    <p> 
        Return to <a href="${pageContext.request.contextPath}/HomeServlet">home &nbsp <i class="fa fa-home" aria-hidden="true"></i></a>
    </p>

</body>
</html>