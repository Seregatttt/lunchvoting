<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login FormLogin Form</title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<div class="box">
    <form id="login_form" action="spring_security_check" method="post">
        <h2>Форма авторизации</h2>
        <input class="form-control mr-1 mb10"  type="text" placeholder="Email" name="username">
        <input class="form-control mr-1 mb10" type="password" placeholder="Password" name="password">
        <button class="btn btn-success mb10" type="submit" >Sign In</button>
    </form>
    <form action="logout" method="post">
        <input class="btn btn-primary" type="submit" value="Sign Out"/></form>
</div>
</body>
</html>
