<#escape x as x!"">
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
</head>
<body>
	<div class="container">
      <form action="${request.contextPath}/login.html" method="post">
        <h2 class="form-signin-heading"></h2>
        <label for="userName" class="sr-only">用户名</label>
        <input type="text" id="userName" name="userName" class="form-control" placeholder="用户名" required autofocus>
        <label for="password" class="sr-only">密码</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="密码" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
      </form>
    </div>
</body>
</html>
</#escape>