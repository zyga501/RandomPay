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
        background-color:#c01116;
        background-position:center top;
        background-image: url(<%=request.getContextPath()%>/image/top.png)  ;
        background-repeat: no-repeat;
        filter:"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale')";
        -moz-background-size:100% ;
        background-size:100% ;
    }
    #dlgReply
    {
        width:100%;
        position:absolute;
        top:25%;
        height:75%;
        text-align: center;
        vertical-align: middle;
    }
    table
    {
        width:92%;
        height:90%;
        margin-left: auto;
        margin-right: auto;
        border-spacing:0px;
    }
    .bjtr >td{
        width:20%;
        height:20%;
        text-align:center;
        background:url(<%=request.getContextPath()%>/image/hb.png) no-repeat center center;
        background-size:cover;
    }
    img{
        width:100%;
        vertical-align:middle;
    }
    .commpanel{
        -webkit-border-radius: 5px;
        border-radius: 5px;
        background-color: #7b7b81;
        color: #FEFEFE;
        border: none;
        font-size: 18px;
        width: 100%;
        padding: 10px 6px;
    }
    button{
        -webkit-border-radius: 3px;
        border-radius: 3px;
        background-color: #666666;
        color: #06af3f;
        border: none;
        font-size: 18px;
        width: 33%;
        padding: 10px 6px;
    }
    .nav_list{width:100%;height:38px;background:#2E2D3C;position:absolute; bottom:0px;}
    .msgpanel {  height:60px;
        filter:alpha(Opacity=80);/*支持 IE 浏览器*/
        -moz-opacity:0.5;/*支持 FireFox 浏览器*/
        opacity: 0.5;/*支持 Chrome, Opera, Safari 等浏览器*/
        z-index:100;
        background-color:#ffffff;  }
</style>
    <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/layer.min.js"></script>
    <script>
        $().ready(function(){GetRandomNum();
            $('.bjtr td').on('click', function(){
                randomPay($(this).attr("id"));
            });
        });

        function paychoose() {
            $.ajax({
            type: 'post',
            url: '<%=request.getContextPath()%>/weixin/Pay!checkBonus',
            dataType:"json",
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.resultCode == "Failed") {
                    layer.open({
                        type: 2,
                        title: '选择支付种类',
                        shadeClose: true,
                        shade: 0.8,
                        area: ['380px', '400px'],
                        content: 'choosepay.jsp' //iframe的url
                    });
                }
            }
            })
        };

        function makeqcode() {
            layer.open({
                type: 2,
                title: '微信支付，赢红包',
                shadeClose: true,
                shade: 0.8,
                area: ['380px', '400px'],
                content: 'Pay!makeQcode' //iframe的url
            });
        };

        function showcomm(){
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!getCommission',
                dataType:"json",
                success: function (data) {
                    var json = eval("(" + data + ")");
                    var v = 0;
                    if (json.comm!=undefined)
                      v=json.comm;
                    layer.open({
                        type: 1,
                        title:"我的佣金",
                        shade: 0.8, //遮罩透明度
                        area: ['380px', '100px'],
                        content: '<div style="position: absolute;width:100%;top: 30%"><span class="commpanel">累计未结算佣金:'+v+'元</span></div>'
                    });
                }
            })
        }

        var boolclick = false;
        function randomPay(v){
            if (boolclick)
            return;
            boolclick = true;
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!randomPay',
                dataType:"json",
                data:{itempos: v.substring(1, v.length)},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.State == "NoData") {
                        alert("没有找到付款信息，请先付款，如果已经付款请稍等片刻再次点击！")
                    }
                    else {
                        for(var i=1;i<=json.hblist.length;i++){
                            $('.bjtr td#t'+i).text(json.hblist[i-1]);
                        }
                        alert(json.State);
                        for(var i=1;i<=15;i++){
                            $('.bjtr td#t'+i).text("");
                        }
                    }
                    boolclick = false;
                },error: function(){
                    boolclick = false;
                }
            })
        }

        function GetRandomNum()
        {
            var Range = 1000;
            var rt="恭喜:<br>";
            for (i=1;i<10;i++) {
                var Rand = Math.random();
                var Rand2 = Math.random();
                var Rand3= Math.random();
                var Rand4 = Math.random();
                var v= parseFloat(Math.round(Rand2.toFixed(4) * Rand3.toFixed(4)* Rand4.toFixed(4)*Rand.toFixed(4) * 1800))+parseFloat(Rand2.toFixed(2));
                rt += "&nbsp &nbsp wx" + "****" + (Math.round(Rand.toFixed(3) * Range)) + "：获得" + v.toFixed(2) + "元<br>";
            }
            $("#msg").html(rt);
        }
    </script>
</head>
<body >
<div id="divparent">
    <div id="dlgReply">
        <table >
            <tr style="height: 15px;">
                <td colspan=5 align="center" valign="middle">
                    <!--button class="button" onclick="paychoose()">付     款</button-->
                <div class="msgpanel"><marquee direction="up" scrolldelay="2" scrollamount="1" height="100%"><div id="msg"></div></marquee></div>
                </td>
            </tr>
            <tr class = "bjtr">
                <td id="t1"></td>
                <td id="t2"></td>
                <td id="t3"></td>
                <td id="t4"></td>
                <td id="t5"></td>
            </tr>
            <tr class = "bjtr" >
                <td id="t6"></td>
                <td id="t7"></td>
                <td id="t8"></td>
                <td id="t9"></td>
                <td id="t10"></td>
            </tr>
            <tr class = "bjtr">
                <td id="t11"></td>
                <td id="t12"></td>
                <td id="t13"></td>
                <td id="t14"></td>
                <td id="t15"></td>
            </tr>
        </table>
    </div>
</div>
<div class="nav_list"><button onclick="paychoose()">我要消费</button><button onclick="makeqcode()" >生成推广码</button><button onclick="showcomm()">我的佣金</button></div>
</body>
</html>
