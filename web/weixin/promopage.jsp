<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JS生成二维码</title>
    <style>
        html,body {
            margin:0 auto;
            text-align: center;
        }
    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.qrcode.js"></script>
     
</head>
<body>
<div id="output" style="display: none;"></div>
<div id="imgdiv"><img src="" id="qcodeq"> </div>
<div id="text"></div>
</body>
<script>
    if("${session.wxid}"=="" ){
        $('#output').html("请重新扫码");
    }
    else{
        $('#output').qrcode({
        width:200,
        height:200,
        correctLevel:0,
        foreground:"#ff0000",
        text:"http://"+window.location.host+"<%=request.getContextPath()%>/weixin/Pay!mainPage?tid=${session.wxid}"
        });
        $("#text").html("<br><br><span>保存您的专属二维码到手机，推荐朋友</span><br>"+
            "<span>通过扫扫参与游戏即可获得推广奖励</span>");
        wwr();
    }

    function wwr(){
        $("#qcodeq").attr("src", $('#output').find('canvas')[0].toDataURL("image/png"));

    }

</script>
</html>