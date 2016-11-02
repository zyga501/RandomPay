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
    <script>
        var json;
        var pagenum = 0;
        var htmlstr = "";
        function getbonuslist() {
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!getBonusList',
                dataType: "json",
                success: function (data) {
                    json = eval("(" + data + ")");
                    if (json.resultCode == "Succeed") {
                        var htmlstr = "<ul id='listcontent'>";
                        for (var j = (pagenum * 10); j < Math.min((pagenum + 1) * 10, json.length); j++) {
                            htmlstr += '<li> <span class="date">json[j].timeend</span>';
                            htmlstr += '    <span class="text">json[j].bonus</span>';
                            htmlstr += '   <span class="text">已支付</span></li>';
                        }
                        htmlstr += '</ul>';
                        $(".person_list").html(htmlstr);
                        $(".person_list").attr("display", "display");
                    }
                }
            })

            function morelist() {
                htmlstr = '';
                for (var j = (pagenum * 10); j < Math.min((pagenum + 1) * 10, json.length); j++) {
                    htmlstr += '<li> <span class="date">json[j].timeend</span>';
                    htmlstr += '    <span class="text">json[j].bonus</span>';
                    htmlstr += '   <span class="text">json[j].state</span></li>';
                }
                if (htmlstr != "") {
                    var s = document.getElementById('listcontent');
                    s.appendChild(htmlstr);
                }
            }

        }

    </script>
</head>

<body>
<div class="wrap">
    <div class="person_top">
    	<span class="line_bg">
        	总佣金：<font><%=request.getSession().getAttribute("paidcomm") == null ? 0 : request.getSession().getAttribute("paidcomm")%>
        </font><i></i>
        </span>
        <span>
        	待发佣金：<font><%=request.getSession().getAttribute("comm") == null ? 0 : request.getSession().getAttribute("comm")%>
        </font>
        </span>
    </div>
    <div class="person_content">
        <div class="person_btn">
            <ul>
                <li>
                    <a href="#" onclick="getbonuslist()">
                        <img src="images/buy_car_icon.png">
                        红包记录
                    </a>
                </li>
                <li>
                    <a href="person_list.html">
                        <img src="images/money_icon.png">
                        佣金记录
                    </a>
                </li>
                <li>
                    <a href="link_us.html">
                        <img src="images/phone_icon.png">
                        联系我们
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div class="person_content" style="display:none">
        <div class="person_title">
            <span>时间</span>
            <span>红包金额</span>
            <span>支付状态</span>
        </div>
        <div class="clear"></div>
        <div class="person_list">
            <div class="clear"></div>
            <div class="more_btn">
                <a href="javascript:;" onclick="morelist()">加载更多</a>
            </div>
        </div>
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
