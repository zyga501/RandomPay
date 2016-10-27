<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp">
    <title>扫红码管理平台</title>
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->

    <link rel="shortcut icon" href="">
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/css/animate.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/css/style.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/layer.css" id="layui_layer_skinlayercss">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/layer.ext.css" id="layui_layer_skinlayerextcss">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" id="layui_layer_skinmoonstylecss">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        function updateCommRate() {
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/web!updateCommRate',
                dataType: "json",
                data: $("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.resultCode == "Succeed") {
                        alert("设置成功!");
                    }
                    else
                        alert("设置失败，请确认输入是否正确!");
                }
            })
        }
    </script>
</head>

<body ><br><br><br><form class="form-horizontal" role="form">
<div id="ooo" class="form-group">
    <label for="commRate" class="col-sm-2 control-label">佣金比例：</label>
    <div class="col-sm-7">
        <input type="text" class="form-control" id="commRate" name="commRate"
               placeholder="大于0，小于1">
    </div><button class="col-sm-3 btn btn-danger" onclick="updateCommRate()">确定</button>
</div></form>
</body>
</html>