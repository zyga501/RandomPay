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
        var type_;
        function getbonuslist() {
            type_ ="bonus";
            $("#tabtt").text("红包金额");
            pagenum = 0;
            htmlstr = "";
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!getBonusList',
                dataType: "json",
                success: function (data) {
                    json = eval("(" + data + ")");
                    {
                        var htmlstr = "<ul id='ullist'>";
                        for (var j = (pagenum * 10); j < Math.min((pagenum + 1) * 10, json.length); j++) {
                            htmlstr += '<li> <span class="date">'+json[j].timeend+'</span>';
                            htmlstr += '    <span class="text">'+json[j].bonus/100.00+'</span>';
                            htmlstr += '   <span class="text">已支付</span></li>';
                        }
                        htmlstr += '</ul>';
                        $("#listcontent").html(htmlstr);
                        $("#listcontent").css("display", "block");
                        $("#listblock").css("display", "block");
                        $(".person_btn").css("display", "none");
                    }
                }
            })
        }

        function morelist() {
            pagenum+=1;
            if (((pagenum)*10)>=json.length) {
                $(".more_btn").html("");
                return ;
            }
            htmlstr = '';
            for (var j = (pagenum * 10); j < Math.min((pagenum + 1) * 10, json.length); j++) {
                htmlstr = '<li> <span class="date">' + json[j].timeend + '</span>';
                if (type_ == "bonus") {
                htmlstr += '    <span class="text">' + json[j].bonus / 100.00 + '</span>';
                htmlstr += '   <span class="text">已支付</span></li>';
            }
                else
            {
                htmlstr += '    <span class="text">' + json[j].comm / 100.00 + '</span>';
                var v =json[j].status==1?"已支付":json[j].status==0?"未支付":"已作废";
                htmlstr += '   <span class="text">'+v+'</span></li>';
            }
                $('#ullist').append(htmlstr);
            }
        }

        function getcommlist() {
            type_ ="comm";
            $("#tabtt").text("佣金金额");
            pagenum = 0;
            htmlstr = "";
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!getCommList',
                dataType: "json",
                success: function (data) {
                    json = eval("(" + data + ")");
                    {
                        var htmlstr = "<ul id='ullist'>";
                        for (var j = (pagenum * 10); j < Math.min((pagenum + 1) * 10, json.length); j++) {
                            htmlstr += '<li> <span class="date">'+json[j].timeend+'</span>';
                            htmlstr += '    <span class="text">'+json[j].comm/100.00+'</span>';
                            var v =json[j].status==1?"已支付":json[j].status==0?"未支付":"已作废";
                            htmlstr += '   <span class="text">'+v+'</span></li>';
                        }
                        htmlstr += '</ul>';
                        $("#listcontent").html(htmlstr);
                        $("#listcontent").css("display", "block");
                        $("#listblock").css("display", "block");
                        $(".person_btn").css("display", "none");
                    }
                }
            })
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
                        <img src="<%=request.getContextPath()%>/images/buy_car_icon.png">
                        红包记录
                    </a>
                </li>
                <li>
                    <a href="#" onclick="getcommlist()">
                        <img src="<%=request.getContextPath()%>/images/money_icon.png">
                        佣金记录
                    </a>
                </li>
                <li>
                    <a href="linkme.jsp">
                        <img src="<%=request.getContextPath()%>/images/phone_icon.png">
                        联系我们
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div class="person_content" id="listblock" style="display:none">
        <div class="person_title">
            <span>时间</span>
            <span id="tabtt">红包金额</span>
            <span>支付状态</span>
        </div>
        <div class="clear"></div>
        <div class="person_list">
            <div id="listcontent"></div>
            <div class="clear"></div>
            <div class="more_btn">
                <a href="#" onclick="morelist();">加载更多</a>
            </div>
        </div>
    </div>
</div>
<div class="footer">
    <a href="choosepay.jsp">去支付</a>
    <a href="randompay.jsp">抢红包</a>
    <a href="Pay!makeQcode">代理</a>
    <a href="Pay!getCommission">个人中心</a>
</div>
</body>
</html>
