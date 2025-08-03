<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 20px;
        }

        form {
            max-width: 400px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        input[type='text'], input[type='password'], input[type='submit'] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 3px;
            box-sizing: border-box;
        }

        input[type='submit'] {
            background-color: #4caf50;
            color: white;
            cursor: pointer;
        }

        input[type='submit']:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
	<h1><CENTER>Login page</CENTER></h1>
	<form action="Login" method="post">
		Enter email: <input type="text" name = "email1"><br><br>
		Enter password: <input type = "text" name = "pass1"><br><br>
		<input type = "submit" value="login">
	</form>
</body>
</html>