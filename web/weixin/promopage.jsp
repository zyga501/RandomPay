<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <title>首页</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
    <script type="text/javascript" language="javascript"
            src="<%=request.getContextPath()%>/js/jquery-1.12.1.min.js"></script>
    <script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/js/fontSize.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.qrcode.js"></script>
    <style>
        #divparent {
            width: 100%;
            height: 100%;
            background-color: #b5b5c0;
            background-position: center top;
            background-image: url(<%=request.getContextPath()%>/image/top.png);
            background-repeat: no-repeat;
            filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale')";
            -moz-background-size: 100%;
            background-size: 100%;
        }
    </style>
</head>

<body>
<div class="wrap">
    <div class="link_code">
        <div id="output" style="display: none;"></div>
        <div id="imgdiv"><img src="" id="qcodeq">
        </div>
    </div>
    <div class="link_text">保存您的专属二维码到手机，推荐朋友通过扫扫参与游戏即可获得推广奖励</div>
    <div class="footer">
        <a href="choosepay.jsp">去支付</a>
        <a href="randompay.jsp">抢红包</a>
        <a href="Pay!makeQcode">代理</a>
        <a href="Pay!getCommission">个人中心</a>
    </div>
</div>
</body>

<script>
    $().ready(
            function () {
                if ("${session.wxid}" == "") {
                    alert("请重新扫码");
                }
                else {
                    $('#output').qrcode({
                        width: 200,
                        height: 200,
                        correctLevel: 0,
                        foreground: "#ff0000",
                        text: "http://" + window.location.host + "<%=request.getContextPath()%>/weixin/Pay!mainPage?tid=${session.wxid}"
                    });
                    wwr();
                }
            });

    function wwr() {
        $("#qcodeq").attr("src", $('#output').find('canvas')[0].toDataURL("image/png"));
        $('#output').html("");
    }
</script>
</html>
