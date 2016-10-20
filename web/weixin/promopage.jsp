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
<div id="output"></div>
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
        text:"http://weixin.com?wxid=${session.wxid}"
    });}
</script>
</html>