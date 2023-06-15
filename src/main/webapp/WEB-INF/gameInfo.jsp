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
<button id="lookCardButton" onclick="buttonReq('lookCardButton')">看牌</button>
<button id="abandonCardButton" onclick="buttonReq('abandonCardButton')">弃牌</button>
<button id="readyButton" onclick="buttonReq('readyButton')">准备</button>


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
                if (responseData.data == '100'){
                    alert("玩家信息失效")
                    window.location.href = "index";
                }
                if (responseData.success == true){
                    pageBuild(responseData.data);
                }else {
                    alert(responseData.message);
                }
            }
        };
        xhr.open("POST", "/game/getInfo", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        xhr.send(JSON.stringify(reqParam));
    }

    /**
     * 页面渲染
     */
    function pageBuild(data) {
        const showText = document.getElementById("showText");
        showText.innerHTML = data.showText;
        if (data.showButtons.includes("readyButton")){
            document.getElementById("readyButton").style.display = "block";
        } else {
            document.getElementById("readyButton").style.display = "none";
        }

        if (data.showButtons.includes("abandonCardButton")){
            document.getElementById("abandonCardButton").style.display = "block";
        } else {
            document.getElementById("abandonCardButton").style.display = "none";
        }

        if (data.showButtons.includes("lookCardButton")){
            document.getElementById("lookCardButton").style.display = "block";
        } else {
            document.getElementById("lookCardButton").style.display = "none";
        }
    }


    /**
     * 按钮提交信息
     * type
     */
    function buttonReq(type) {
        const reqParam = {};
        reqParam['userToken'] = userToken;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.data == '100'){
                    alert("玩家信息失效")
                    window.location.href = "index";
                }
                if (responseData.success == true){
                    loadPage();
                }else {
                    alert(responseData.message);
                }
            }
        };
        xhr.open("POST", "/game/buttonReq", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        xhr.send(JSON.stringify(reqParam));
    }


</script>

</html>
