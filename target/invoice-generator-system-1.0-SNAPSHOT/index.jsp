<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome to Invoice Generator</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f0f2f5;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    .container {
        background-color: #fff;
        padding: 40px;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        text-align: center;
        max-width: 400px;
        width: 100%;
    }
    h1 {
        color: #333;
        margin-bottom: 10px;
    }
    p {
        color: #666;
        margin-bottom: 30px;
    }
    .btn-container {
        display: flex;
        flex-direction: column;
        gap: 15px;
    }
    .btn {
        padding: 12px 25px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        text-decoration: none;
        font-size: 16px;
        font-weight: bold;
        transition: background-color 0.3s ease;
    }
    .btn-login {
        background-color: #4CAF50;
        color: white;
    }
    .btn-login:hover {
        background-color: #45a049;
    }
    .btn-register {
        background-color: #2196F3;
        color: white;
    }
    .btn-register:hover {
        background-color: #1e88e5;
    }
    .btn-guest {
        background-color: #f44336;
        color: white;
    }
    .btn-guest:hover {
        background-color: #da190b;
    }
</style>
</head>
<body>
    <div class="container">
        <h1>Welcome to the Invoice Generator!</h1>
        <p>Please select an option to continue.</p>
        <div class="btn-container">
            <a href="Login.jsp" class="btn btn-login">Log In</a>
            <a href="Register.jsp" class="btn btn-register">Register</a>
            <a href="FetchItemsServlet" class="btn btn-guest">Continue as Guest</a>
        </div>
    </div>
</body>
</html>