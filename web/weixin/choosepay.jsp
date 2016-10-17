<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/laypage.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/laydate.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/layer.css" rel="stylesheet" type="text/css"/>
<style>
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

</style>
    <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/layer.min.js"></script>
    <script>
        function pay(b){
            alert(b)
        }
    </script>
</head>
<body scroll="no">
<input type="button" class="but"  value="支付10元"   onclick="pay(1000)" /><br><br>
<input type="button" class="but"  value="支付20元"   onclick="pay(2000)" /><br><br>
<input type="button"  class="but" value="支付50元"   onclick="pay(5000)" /><br><br>
<input type="button"  class="but" value="支付100元"   onclick="pay(10000)" /><br>
</body>
</html>
