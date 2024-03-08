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
    <style>
        #showText {
            white-space: pre-line;
        }

        button {
            display: inline-block;
            margin: 0 10px;
            background-color: #137f44;
            color: #fff;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
            transition: all 0.3s ease-in-out;
        }

        button:hover {
            background-color: #00cc0a;
        }

        button:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }

        button:first-of-type {
            margin-right: 10px;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            flex-direction: column;
        }

        .button-container {
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
    <title>实时信息</title>
</head>
<body onload="loadPage()">
<div class="container">
    <h3>实时信息</h3>
    <%-- 显示页面内容 --%>
    <div id="showText"></div>
    <br>
    <div class="button-container">
        <button id="refreshButton" onclick="loadPage()">刷新</button>
        <%-- 显示或隐藏按钮 --%>
        <button id="lookCardButton" onclick="buttonReq('lookCardButton')">看牌</button>
        <button id="abandonCardButton" onclick="buttonReq('abandonCardButton')">弃牌</button>
        <button id="readyButton" onclick="buttonReq('readyButton')">准备</button>
        <button id="startGameButton" onclick="buttonReq('startGameButton')">开始游戏</button>
        <button id="startNextGameButton" onclick="buttonReq('startNextGameButton')">开始新一局游戏</button>
        <button id="restartGameButton" onclick="buttonReq('restartGameButton')">重启游戏</button>
    </div>
</div>

</body>

<script type="text/javascript">
    var userToken = localStorage.getItem('userToken');
    //没有 token 跳转到index
    if (!userToken) {
        alert("请先注册信息")
        window.location.href = "index";
    }

    //刷新信息 若返回 userToken 失效，则清空 token 并跳转到登录页面

    function loadPage() {
        const reqParam = {};
        reqParam['userToken'] = userToken;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.data == '100') {
                    window.location.href = "index";
                }
                if (responseData.success == true) {
                    pageBuild(responseData.data);
                } else {
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
        showText.innerHTML = formatText(data.showText);
        if (data.showButtons.includes("readyButton")) {
            document.getElementById("readyButton").style.display = "block";
        } else {
            document.getElementById("readyButton").style.display = "none";
        }

        if (data.showButtons.includes("abandonCardButton")) {
            document.getElementById("abandonCardButton").style.display = "block";
        } else {
            document.getElementById("abandonCardButton").style.display = "none";
        }

        if (data.showButtons.includes("lookCardButton")) {
            document.getElementById("lookCardButton").style.display = "block";
        } else {
            document.getElementById("lookCardButton").style.display = "none";
        }
        if (data.showButtons.includes("startGameButton")) {
            document.getElementById("startGameButton").style.display = "block";
        } else {
            document.getElementById("startGameButton").style.display = "none";
        }

        if (data.showButtons.includes("startNextGameButton")) {
            document.getElementById("startNextGameButton").style.display = "block";
        } else {
            document.getElementById("startNextGameButton").style.display = "none";
        }

        if (data.showButtons.includes("restartGameButton")) {
            document.getElementById("restartGameButton").style.display = "block";
        } else {
            document.getElementById("restartGameButton").style.display = "none";
        }
    }



    function formatText(text) {
        // 将♥和♦替换为带有红色样式的<span>元素
        text = text.replaceAll('♥', '<span style="color: red;">♥</span>');
        text = text.replaceAll('♦', '<span style="color: red;">♦</span>');
        return text;
    }


    /**
     * 按钮提交信息
     * type
     */
    function buttonReq(type) {
        if (type == 'restartGameButton'){
            let confirmed = confirm('确定要重启游戏吗，这将会清除所有玩家的信息');
            if (!confirmed) {
                return;
            }
        }
        const reqParam = {};
        reqParam['userToken'] = userToken;
        reqParam['buttonType'] = type;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.data == '100') {
                    window.location.href = "index";
                }
                if (responseData.success == true) {
                    loadPage();
                } else {
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
