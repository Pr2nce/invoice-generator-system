<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order History</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f0f2f5;
        }
        .container {
            margin-top: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="mb-4">Your Order History</h2>

        <c:choose>
            <c:when test="${not empty userOrders}">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead class="thead-dark">
                            <tr>
                                <th>Transaction ID</th>
                                <th>Item</th>
                                <th>Price</th>
                                <th>Quantity</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${userOrders}">
                                <tr>
                                    <td>${order.transactionId}</td>
                                    <td>${order.item}</td>
                                    <td>$${order.price}</td>
                                    <td>${order.quantity}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info" role="alert">
                    You have not placed any orders yet.
                </div>
            </c:otherwise>
        </c:choose>
        
        <a href="FetchItemsServlet" class="btn btn-primary mt-3">Back to Items</a>
    </div>
</body>
</html>