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
<button id="submitButton">注册</button>
<button id="loginButton">登录</button>
</body>

<script type="text/javascript">
    // JavaScript 代码
    const userName = document.getElementById("userName");
    const password = document.getElementById("password");
    const submitButton = document.getElementById("submitButton");
    const loginButton = document.getElementById("submitButton");

    submitButton.addEventListener("click", () => {
        const reqParam = {};
        reqParam['userName'] = userName.value;
        reqParam['password'] = password.value;
        // 发送请求
        fetch("/manager/player/register", {
            method: "POST",
            body: JSON.stringify(reqParam),
            headers: {
                "Content-Type": "application/json",
            },
        }).then((response) => response.json())
            .then((responseData) => {
                if (responseData.success == true) {
                    // 将返回数据存储到本地存储中
                    localStorage.setItem("userToken", JSON.stringify(responseData.data));
                    console.log("数据已存储到本地存储中");
                    //跳转到实时信息页面
                    window.location.href = "gameInfo";
                } else {
                    alert(responseData.message);
                }
            })
            .catch((error) => {
                console.error("请求出错:", error);
                alert("请求服务器错误");
            });
    });

    loginButton.addEventListener("click", () => {
        const reqParam = {};
        reqParam['userName'] = userName.value;
        reqParam['password'] = password.value;
        // 发送请求
        fetch("/manager/player/login", {
            method: "POST",
            body: JSON.stringify(reqParam),
            headers: {
                "Content-Type": "application/json",
            },
        }).then((response) => response.json())
            .then((responseData) => {
                if (responseData.success == true) {
                    // 将返回数据存储到本地存储中
                    localStorage.setItem("userToken", JSON.stringify(responseData.data));
                    console.log("数据已存储到本地存储中");
                    //跳转到实时信息页面
                    window.location.href = "gameInfo";
                } else {
                    alert(responseData.message);
                }
            })
            .catch((error) => {
                console.error("请求出错:", error);
                alert("请求服务器错误");
            });
    });

</script>

</html>
