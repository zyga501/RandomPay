<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        function updateWeixinIdById() {
            $.ajax({
                type: 'post',
                url: 'merchant/SubMerchantUser!updateWeixinIdById',
                dataType:"json",
                data:{id: "<%=request.getParameter("state")%>", code : "<%=request.getParameter("code")%>"},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.resultCode == 'Succeed') {
                        $('#Message').val("绑定成功！");
                    }
                    else {
                        $('#Message').val("绑定失败!");
                    }
                }
            })
        }
        $(function(){
            updateWeixinIdById();
        })
    </script>
</head>
<body>
<center>
    <input id="Message" type="button" onclick="WeixinJSBridge.call('closeWindow');" /></center>
</body>
</html>
