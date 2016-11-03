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
</head>

<body>
<div class="wrap">
    <div class="link_code">
        <div id="imgdiv"><img src="<%=request.getContextPath()%>/images/linkme.jpg" id="qcodeq">
        </div>
    </div>
    <div class="link_text"></div>
    <div class="footer">
        <a href="choosepay.jsp">去支付</a>
        <a href="randompay.jsp">抢红包</a>
        <a href="Pay!makeQcode">代理</a>
        <a href="Pay!getCommission">个人中心</a>
    </div>
</div>
</body>

</html>
