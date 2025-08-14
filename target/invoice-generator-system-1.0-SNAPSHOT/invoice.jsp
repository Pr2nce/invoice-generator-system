<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice</title>
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
        <h2 class="mb-4">Invoice</h2>
        
        <p><strong>Invoice Number:</strong> ${transactionId}</p>
        <p><strong>Date:</strong> ${invoiceDate}</p>
        <p><strong>Customer Name:</strong> ${customerName}</p>
        <p><strong>Phone Number:</strong> ${phoneNumber}</p>
        
        <hr>

        <h4>Items</h4>
        <div class="table-responsive mb-4">
            <table class="table table-striped table-bordered">
                <thead class="thead-dark">
                    <tr>
                        <th>Item</th>
                        <th>Quantity</th>
                        <th>Total Price</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${orderedItems}">
                        <tr>
                            <td>${item.name}</td>
                            <td>${item.quantity}</td>
                            <td>$${item.totalPrice}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <hr>
        
        <p><strong>Subtotal:</strong> $${subtotal}</p>
        <p><strong>Tax (${taxRate}%):</strong> $${taxAmount}</p>
        <p><strong>Discount (${discountRate}%):</strong> $${discountAmount}</p>
        <p><h3>Final Total:</h3> <h3>$${finalTotal}</h3></p>
        
        <hr>

        <form action="PlaceOrderServlet" method="get">
            <input type="hidden" name="action" value="download">
            <input type="hidden" name="transactionId" value="${transactionId}">
            <button type="submit" class="btn btn-success">Download PDF</button>
            <a href="FetchItemsServlet" class="btn btn-primary">Back to Items</a>
        </form>
    </div>
</body>
</html>