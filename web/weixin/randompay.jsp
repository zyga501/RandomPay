<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/laypage.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/laydate.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/layer.css" rel="stylesheet" type="text/css"/>
<style>
    #divparent
    {
        width:100%;height:100%;
        background-color:#c01116;
        background-position:center top;
        background-image: url(<%=request.getContextPath()%>/image/top.png)  ;
        background-repeat: no-repeat;
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
        height:99%;
        margin-left: auto;
        margin-right: auto;
        border-spacing:0px;
    }
    .bjtr >td{
        width:20%;
        height:23%;
        text-align:center;
        background:url(<%=request.getContextPath()%>/image/hb.png) no-repeat center center;
        background-size:cover;
    }
    img{
        width:100%;
        vertical-align:middle;
    }
    .button{
        width:80%;
        color:#f20903;
        background-color:#9ad336;
        font-size:32px;
    }
</style>
    <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/layer.min.js"></script>
    <script>
        function paychoose() {
            layer.open({
            type: 2,
            title: 'Pay',
            shadeClose: true,
            shade: 0.8,
            area: ['380px', '90%'],
            content: 'choosepay.jsp' //iframe的url
        });
        };
        function randomPay(){
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/weixin/Pay!randomPay',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.State == "NoData") {
                        alert("没有找到付款信息，请先付款，如果已经付款请稍等片刻再次点击！")
                    }
                    else {
                        alert(json.State);
                    }
                }
            })
        }
    </script>
</head>
<body scroll="no">
<div id="divparent">
    <div id="dlgReply">
        <table >
            <tr style="height: 15px;">
                <td colspan=5 align="center" valign="middle">
                    <button class="button" onclick="paychoose()">付     款</button></td>
            </tr>
            <tr class = "bjtr" onclick="randomPay()">
                <td onclick="">
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
            </tr>
            <tr class = "bjtr" onclick="randomPay()">
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
            </tr>
            <tr class = "bjtr" onclick="randomPay()">
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>
                    
                </td>
                <td>

                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
