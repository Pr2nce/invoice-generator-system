<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Logout</title>
</head>
<body>
    <%
        // Invalidate the current session
        session.invalidate();
        // Redirect the user to the home page
        response.sendRedirect("index.jsp");
    %>
</body>
</html>