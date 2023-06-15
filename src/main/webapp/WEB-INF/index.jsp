<%--
  Created by IntelliJ IDEA.
  User: pn
  Date: 2022/3/25
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册</title>
</head>
<body>
<!-- HTML 页面 -->
<input type="text" id="userName" placeholder="用户名"/>
<input type="text" id="password" placeholder="密码"/>
<button id="submitButton" onclick="register()">注册</button>
<button id="loginButton" onclick="login()">登录</button>
</body>

<script type="text/javascript">
    // JavaScript 代码
    const userName = document.getElementById("userName");
    const password = document.getElementById("password");
    const submitButton = document.getElementById("submitButton");
    const loginButton = document.getElementById("submitButton");

    function register(){
        const reqParam = {};
        reqParam['userName'] = userName.value;
        reqParam['password'] = password.value;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.success == true) {
                    // 将返回数据存储到本地存储中
                    localStorage.setItem("userToken", responseData.data);
                    console.log("数据已存储到本地存储中" + responseData.data);
                    //跳转到实时信息页面
                    window.location.href = "gameInfo";
                } else {
                    alert(responseData.message);
                }
            }
        };
        xhr.open("POST", "/manager/player/register", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        xhr.send(JSON.stringify(reqParam));
    }

    function login(){
        const reqParam = {};
        reqParam['userName'] = userName.value;
        reqParam['password'] = password.value;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.success == true) {
                    // 将返回数据存储到本地存储中
                    localStorage.setItem("userToken", JSON.stringify(responseData.data));
                    console.log("数据已存储到本地存储中");
                    //跳转到实时信息页面
                    window.location.href = "gameInfo";
                } else {
                    alert(responseData.message);
                }
            }
        };
        xhr.open("POST", "/manager/player/login", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        xhr.send(JSON.stringify(reqParam));
    }

</script>

</html>
