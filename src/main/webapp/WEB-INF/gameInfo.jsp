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
    <title>实时信息</title>
</head>
<body onload="loadPage()">
<!-- HTML 页面 -->
<h1>实时信息</h1>

<%-- 显示页面内容 --%>
<div id="showText"></div>

<button id="refreshButton" onclick="loadPage()">刷新</button>
<%-- 显示或隐藏按钮 --%>
<button id="lookCardButton" onclick="buttonReq('1')">看牌</button>
<button id="abandonCardButton" style="display:none;" onclick="buttonReq('2')">弃牌</button>


</body>

<script type="text/javascript">
    var userToken = localStorage.getItem('userToken');
    //没有 token 跳转到index
    if (!userToken) {
        alert("请先注册信息")
        window.location.href = "index";
    }
    //刷新信息 若返回 userToken 失效，则清空 token 并跳转到登录页面

    function loadPage () {
        const reqParam = {};
        reqParam['userToken'] = userToken;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.success == true){
                    if (responseData.data == '100'){
                        alert("玩家信息失效")

                    }

                    pageBuild(data);
                }else {
                    alert(responseData.message);
                }
            }
        };
        xhr.open("POST", "/user", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        xhr.send(JSON.stringify(user));
    }

    /**
     * 页面渲染
     */
    function pageBuild(data) {
        var showText = document.getElementById("showText");
        showText.innerHTML = data;
        if (user.age >= 18) {
            //显示
            document.getElementById("lookCardButton").style.display = "block";
            //隐藏
            document.getElementById("lookCardButton").style.display = "none";
        }
    }


    /**
     * 按钮提交信息
     * type
     */
    function buttonReq(type) {
        var userName = document.getElementById("userName").value;
        var userAge = document.getElementById("userAge").value;
        var user = {name: userName, age: userAge};
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {


                loadPage();
            }
        };
        xhr.open("POST", "/user", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        xhr.send(JSON.stringify(user));
    }


</script>

</html>
