<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your Items</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="text-center">Your Items</h2>
            <a href="logout.jsp" class="btn btn-danger">Logout</a>
        </div>

        <div class="card p-4 mb-4">
            <h4>Manage Your Items</h4>
            <form action="UpdateItemsServlet" method="post">
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="itemName">Item Name:</label>
                        <input type="text" class="form-control" name="itemName" required>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="price">Price:</label>
                        <input type="number" class="form-control" name="price" min="0" step="0.01" required>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="availableQuantity">Quantity:</label>
                        <input type="number" class="form-control" name="availableQuantity" min="0" required>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-4">
                        <button type="submit" name="action" value="add" class="btn btn-success btn-block">Add Item</button>
                    </div>
                    <div class="form-group col-md-4">
                        <button type="submit" name="action" value="update" class="btn btn-primary btn-block">Update Item</button>
                    </div>
                    <div class="form-group col-md-4">
                        <button type="submit" name="action" value="delete" class="btn btn-danger btn-block">Delete Item</button>
                    </div>
                </div>
            </form>
        </div>

        <c:choose>
            <c:when test="${not empty items}">
                <div class="card p-4">
                    <h4 class="mb-3">Available Items for Invoice Generation:</h4>
                    <form action="PlaceOrderServlet" method="post">
                        <div class="form-group">
                            <label for="customerName">Customer Name:</label>
                            <input type="text" class="form-control" id="customerName" name="customerName" required>
                        </div>
                        <div class="form-group">
                            <label for="phoneNumber">Phone Number:</label>
                            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required pattern="\d{10}">
                        </div>
                        <div class="form-group">
                            <label for="taxRate">Tax Rate (%):</label>
                            <input type="number" class="form-control" id="taxRate" name="taxRate" min="0" step="0.01" value="0">
                        </div>
                        <div class="form-group">
                            <label for="discountRate">Discount (%):</label>
                            <input type="number" class="form-control" id="discountRate" name="discountRate" min="0" step="0.01" value="0">
                        </div>
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
                    <form action="FetchUserHistoryServlet" class="mt-3">
                        <button type="submit" class="btn btn-info btn-block">View Order History</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info" role="alert">
                    You have no items in your inventory. Add some to get started.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>