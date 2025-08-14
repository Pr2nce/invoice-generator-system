<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f0f2f5;
            padding-top: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-center">Admin Dashboard</h2>
        <div class="list-group">
            <a href="UpdateItemsServlet" class="list-group-item list-group-item-action">Manage Inventory (Add, Update, Delete)</a>
            <a href="#" class="list-group-item list-group-item-action">View Sales Reports (Coming Soon)</a>
            <a href="logout.jsp" class="list-group-item list-group-item-action text-danger">Logout</a>
        </div>
    </div>
</body>
</html>