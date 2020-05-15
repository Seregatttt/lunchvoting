<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login Form</title>
</head>
<body>
<form id="login_form" action="spring_security_check" method="post">
    <h2>Форма авторизации</h2>
    <input class="form-control mr-1" type="text" placeholder="Email" name="username">
    <input class="form-control mr-1" type="password" placeholder="Password" name="password">
    <button class="btn btn-success" type="submit">submit</button>
</form>
</body>
</html>
