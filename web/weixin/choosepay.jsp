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
        background-image: url(/image/top.png)  ;
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
        background-color:#f0f0f0;
        padding: 10px ;
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
        function pay(v) {
            layer.msg("we234234dsfsdfsdf");
            layer.confirm("请关注公众号等待审核消息<br><img style='width:200px;height:200px'>", {
                btn: ['已经关注'] //按钮
            }, function () {
                window.close();
            })
        };
    </script>
</head>
<body scroll="no">
<div id="divparent">
    <div id="dlgReply">
        <table >
            <tr class = "bjtr">
                <td>
                   <input  value="支付10元"   onclick="pay(1000)" />
                </td>
                <td>支付20元
                </td>
                <td>
                    支付50元
                </td>
                <td>支付100元
                    
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
