<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
<title>首页</title>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
<script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.12.1.min.js"></script>
<script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/js/fontSize.js"></script>
<script> 
  $().ready(function(){ 
  function paychoose() {
            $.ajax({
            type: 'post',
            url: '<%=request.getContextPath()%>/weixin/Pay!checkBonus',
            dataType:"json",
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.resultCode == "Failed") { 
					alert('你可能还有红包尚未领取,请先点击领取');
					history.go(-1);
                }
            }
            })
        };
        });
   function pay(b){
        window.parent.location.href = "<%=request.getContextPath()%>/weixin/jsPayCallback.jsp?total_fee="+b;
    }
</script>
</head>

<body>
<div class="wrap">
	<div class="payment">
    	<ul>
        	<li onclick="pay(1000)">
           		<div class="num01">100</div>
                <div class="num02">封顶</div>
                <div class="num03">仅需要支付<font>10</font>元</div>
                
            </li>
            <li onclick="pay(2000)">
           		<div class="num01">200</div>
                <div class="num02">封顶</div>
                <div class="num03" >仅需要支付<font>20</font>元</div>
                
            </li>
            <li onclick="pay(5000)">
           		<div class="num01">600</div>
                <div class="num02">封顶</div>
                <div class="num03">仅需要支付<font>50</font>元</div>
                
            </li>
            <li onclick="pay(10000)">
           		<div class="num01">1200</div>
                <div class="num02">封顶</div>
                <div class="num03" >仅需要支付<font>100</font>元</div>
                
            </li>
        </ul>
    </div>	
	
</div>
<div class="footer">
    <a href="choosepay.jsp">去支付</a>
    <a href="randompay.jsp">抢红包</a>
    <a href="promopage.jsp">代理</a>
    <a href="infocenter.jsp">个人中心</a>
</div>
</body>
</html>
