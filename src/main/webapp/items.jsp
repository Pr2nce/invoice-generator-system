<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Available Items</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2 class="text-center mb-4">Available Items</h2>
        <div class="card p-4 mx-auto" style="max-width: 600px;">
            <form action="PlaceOrderServlet" method="post">
                <div class="form-group">
                    <label for="customerName">Customer Name:</label>
                    <input type="text" class="form-control" id="customerName" name="customerName" required>
                </div>
                <div class="form-group">
                    <label for="phoneNumber">Phone Number:</label>
                    <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required pattern="\d{10}" title="Please enter a 10-digit phone number">
                </div>
                 <div class="form-group">
                    <label for="taxRate">Tax Rate (%):</label>
                    <input type="number" class="form-control" id="taxRate" name="taxRate" min="0" step="0.01" value="0">
                </div>
                <div class="form-group">
                    <label for="discountRate">Discount (%):</label>
                    <input type="number" class="form-control" id="discountRate" name="discountRate" min="0" step="0.01" value="0">
                </div>
                
                <h4 class="mt-4">Items:</h4>
                <c:forEach var="item" items="${items}">
                    <div class="form-check mb-2">
                        <input type="checkbox" class="form-check-input" id="${item.name}" name="item" value="${item.name}" 
                               <c:if test="${item.availableQuantity == 0}">disabled</c:if>>
                        <label class="form-check-label" for="${item.name}">
                            ${item.name} - $${item.price} (Available: ${item.availableQuantity})
                        </label>
                        <c:if test="${item.availableQuantity > 0}">
                            <div class="ml-4 mt-2">
                                Quantity: <input type="number" class="form-control form-control-sm" name="quantity_${item.name}" min="1" max="${item.availableQuantity}">
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
                
                <button type="submit" class="btn btn-primary btn-block mt-4">Place Order</button>
            </form>
            
            <form action="UpdateItemsServlet" class="mt-3">
                <button type="submit" class="btn btn-success btn-block">Update Items</button>
            </form>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>