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
        $().ready(function () {
            GetRandomNum();
            $('li').on('click', function () {
                randomPay($(this).attr("id"));
            });
        });

        var boolclick = false;
        function randomPay(v) {
            if (boolclick)
                return;
            boolclick = true;
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!randomPay',
                dataType: "json",
                data: {itempos: v.substring(1, v.length)},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.State == "NoData") {
                        $(".pop_bg").css("display", "block");
                    }
                    else {
                        for (var i = 1; i <= json.hblist.length; i++) {
                            $('.bjtr td#t' + i).text(json.hblist[i - 1]);
                        }
                        alert(json.State);
                        for (var i = 1; i <= 15; i++) {
                            $('.bjtr td#t' + i).text("");
                        }
                    }
                    boolclick = false;
                }, error: function () {
                    boolclick = false;
                }
            })
        }

        function GetRandomNum() {
            var Range = 1000;
            var rt = "恭喜:<br>";
            for (i = 1; i < 10; i++) {
                var Rand = Math.random();
                var Rand2 = Math.random();
                var Rand3 = Math.random();
                var Rand4 = Math.random();
                var v = parseFloat(Math.round(Rand2.toFixed(4) * Rand3.toFixed(4) * Rand4.toFixed(4) * Rand.toFixed(4) * 1800)) + parseFloat(Rand2.toFixed(2));
                rt += "&nbsp &nbsp wx" + "****" + (Math.round(Rand.toFixed(3) * Range)) + "：获得" + v.toFixed(2) + "元<br>";
            }
            $("#msg").html(rt);
        }
    </script>
</head>

<body>
<!--弹出层 begin-->
<div class="pop_bg" style="display:none">
    <div class="pop_content">
        <span><br> </span>
        <p>没有找到付款信息，请先付款，如果已经付款请稍等片刻再次点击！</p>
        <div class="pop_btn">
            <a href="javascript:;" onclick='javascript: $(".pop_bg").attr("display","display");'>好</a>
        </div>
    </div>
</div>
<!--弹出层 end-->

<div class="wrap">
    <div class="index_white_bg">
        <marquee direction="up" scrollAmount="1" scrollDelay="1">
            <div id="msg"></div>
        </marquee>
    </div>
    <div class="index_list">
        <ul>
            <li id="t1">
                <div  class="click_text">点击领取&gt;</div>
                <div  class="menoy_icon"></div>
                
            </li>
            <li id="t2">
                <div  class="click_text">点击领取&gt;</div>
                <div class="menoy_icon"></div>
                
            </li>
            <li id="t3">
                <div class="click_text">点击领取&gt;</div>
                <div class="menoy_icon"></div>
                
            </li>
            <li id="t4" >
                <div class="menoy_icon"></div>
                <div class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t5">
                <div class="menoy_icon"></div>
                <div class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t6" >
                <div class="menoy_icon"></div>
                <div class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t7">
                <div class="menoy_icon"></div>
                <div class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t8">
                <div class="menoy_icon"></div>
                <div  class="click_text">点击领取&gt;</div>
                
            </li>
            <li  id="t9">
                <div class="menoy_icon"></div>
                <div class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t10">
                <div class="menoy_icon"></div>
                <div  class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t11">
                <div class="menoy_icon"></div>
                <div  class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t12">
                <div class="menoy_icon"></div>
                <div  class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t13">
                <div class="menoy_icon"></div>
                <div  class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t14" >
                <div class="menoy_icon"></div>
                <div class="click_text">点击领取&gt;</div>
                
            </li>
            <li id="t15">
                <div class="menoy_icon"></div>
                <div class="click_text">点击领取&gt;</div>
                
            </li>
        </ul>
    </div>

</div>
<div class="footer">
    <a href="choosepay.jsp">去支付</a>
    <a href="randompay.jsp">抢红包</a>
    <a href="Pay!makeQcode">代理</a>
    <a href="infocenter.jsp">个人中心</a>
</div>
</body>
</html>
