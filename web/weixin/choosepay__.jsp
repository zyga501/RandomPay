<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/laypage.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/laydate.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/layer.css" rel="stylesheet" type="text/css"/>
<style>
    table
    {
        width:100%;
        height:100%;
        margin-left: auto;
        margin-right: auto;
        border-spacing:0px;
    }
    .but {
        -webkit-border-radius: 5px;
        border-radius: 5px;
        background-color: #06af3f;
        color: #FEFEFE;
        border: none;
        font-size: 18px;
        width: 100%;
        padding: 10px 6px;
    }
    td{
        width:50%;
        height:50%;
        text-align:center;
    }
    img{
        width:80%;
        vertical-align:middle;
    }
    span{
        color: #FF9900;
    }
</style>
    <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/layer.min.js"></script>
    <script>
    function pay(b){
        window.parent.location.href = "<%=request.getContextPath()%>/weixin/jsPayCallback.jsp?total_fee="+b;
    }
</script>
</head>
<body scroll="no">
<div>
    <table>
        <tr>
            <td><img src="<%=request.getContextPath()%>/image/10pay.png" onclick="pay(1000)"></td>
            <td><img src="<%=request.getContextPath()%>/image/20pay.png" onclick="pay(2000)"></td>
        </tr>
        <tr>
            <td><img src="<%=request.getContextPath()%>/image/50pay.png" onclick="pay(5000)"></td>
            <td><img src="<%=request.getContextPath()%>/image/100pay.png" onclick="pay(10000)"></td>
        </tr>
    </table>
    </div>
</body>
</html>
