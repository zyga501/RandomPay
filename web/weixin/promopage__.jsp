<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/laypage.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/laydate.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/layer.css" rel="stylesheet" type="text/css"/>
<style>
    html,body { margin:0 auto;
        text-align: center; }
    #divparent
    {
        width:100%;height:100%;
        background-color: #b5b5c0;
        background-position:center top;
        background-image: url(<%=request.getContextPath()%>/image/top.png)  ;
        background-repeat: no-repeat;
        filter:"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale')";
        -moz-background-size:100% ;
        background-size:100% ;
    }
    #imgdiv{
        position: absolute;
        top:50%;
        width:100%;
    }
</style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.qrcode.js"></script>
    <script src="<%=request.getContextPath()%>/js/layer.min.js"></script>
    <script>
        $().ready(
            function (){
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
        });

        function wwr(){
            $("#qcodeq").attr("src", $('#output').find('canvas')[0].toDataURL("image/png"));

        }
    </script>
</head>
<body >
<div id="divparent">
    <div id="output" style="display: none;"></div>
    <div id="imgdiv"><img src="" id="qcodeq">
        <div id="text"></div> </div>
</div>
</body>
</html>
