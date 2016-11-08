<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <title>首页</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
    <script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/js/fontSize.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.qrcode.js"></script>
    <style>body,
    h1,
    h2,
    h3,
    h4,
    h5,
    h6,
    p,
    dl,
    dd {
        margin: 0;
        -webkit-text-size-adjust: 100%;
        font-family: Helvetica;
    }
    ul,
    ol {
        margin: 0;
        padding: 0;
        list-style: none;
    }
    img {
        display: block;
    }
    a {
        text-decoration: none;
    }
    a,
    input,
    button {
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        -webkit-appearance: none;
        outline: none;
    }
    html,
    body {
        height: 100%;
        position: relative;
        /*overflow: hidden;*/
        background: #eee;
    }
    .clear {
        clear: both;
    }
    .wrap {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
        overflow-x: hidden;
        overflow-y: auto;
        background: url(../images/bg2.png) no-repeat;
        background-size: 100%;
    }
    header{
        text-align:center;
        font:1.4rem/2rem "微软雅黑";
        color:#ea0809;
        padding-top:.8rem;
        font-weight:600;
    }
    .img_code{
        width:6.8rem;
        height:auto;
        margin:.5rem auto 0;
        padding: 3px 3px 3px 3px;
        background-color: #fff;
    }
    .img_code img{
        width:6.8rem;
    }
    .content {
        margin:.8rem auto 0;
        width: 14rem;
        height: 11.2rem;
        border: 2px solid #d8cccc;
        border-radius: 5px;
    }
    .table_box{ padding:.6rem .5rem .2rem;}
    .table_box table {
        width:100%;
        border:1px solid #d8cccc;
        border-collapse: collapse;
        border-spacing: 0;
        color:#000;
    }
    .table_box table tr td{
        white-space:normal;
        word-break:break-all;
        font-size:.5rem;
        color:#000;
        line-height:.8rem;
        border-bottom:1px solid #c6c6c6;
        border-right:1px solid #c6c6c6;
        text-align: center;
        padding:.2rem 0;
    }
    .table_box table tr td font{
        font-weight:600;
        font-size:.8rem;
    }
    .content p{
        padding:.2rem 3rem;
        font-size:.5rem;
        line-height:.8rem;
    }
    .content .footer{
        padding:.2rem .5rem;
        font-size:.6rem;
        line-height:.8rem;
    }
    </style>
</head>
<body>
            <span class="wrap">
                <header>
                    分享代理推广码<br>
                    享丰厚佣金回报
                </header>
                <section class="img_code">
                    <div id="output" style="display: none;"></div>
                    <div id="imgdiv"><img src="" id="qcodeq"></div>
                </section>
                <section class="content">
                    <div class="table_box">
                        <table>
                            <tr>
                                <td><font>代理<br>利润</font></td>
                                <td>单局游戏<br><font>一局</font></td>
                                <td>一级代理<br><font>1.8元</font></td>
                                <td>二级代理<br><font>0.5元</font></td>
                                <td>三级代理<br><font>0.3元</font></td>
                            </tr>
                        </table>
                    </div>
                    <p>
                        佣金丰厚！当天计算佣金如下
                        (10*10*0.8) + (10*10*10*0.5)
                        + (10*10*10*10*0.3) = 3580元
                        这只是推广10人1天的佣金
                    </p>
                </section>
    <div class="footer">
        <a href="choosepay.jsp">去支付</a>
        <a href="randompay.jsp">抢红包</a>
        <a href="Pay!makeQcode">代理</a>
        <a href="Pay!getCommission">个人中心</a>
    </div>
</div>
</body>

<script>
    $().ready(
            function () {
                if ("${session.wxid}" == "") {
                    alert("请重新扫码");
                }
                else {
                    $('#output').qrcode({
                        width: 200,
                        height: 200,
                        correctLevel: 0,
                        foreground: "#ff0000",
                        text: "http://" + window.location.host + "<%=request.getContextPath()%>/weixin/Pay!mainPage?tid=${session.wxid}"
                    });
                    wwr();
                }
            });

    function wwr() {
        $("#qcodeq").attr("src", $('#output').find('canvas')[0].toDataURL("image/png"));
        $('#output').html("");
    }
</script>
</html>
