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
        $(function(){
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/web!getPayReturn',
                dataType: "json",
                data: $("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    $("#rtScale").val(json.rlist[0].rtscale);
                    $("#rta").val(json.rlist[0].paynum);
                    $("#rta1").val(json.rlist[0].rtmin);
                    $("#rta2").val(json.rlist[0].rtmax);
                    $("#rtb").val(json.rlist[1].paynum);
                    $("#rtb1").val(json.rlist[1].rtmin);
                    $("#rtb2").val(json.rlist[1].rtmax);
                    $("#rtc").val(json.rlist[2].paynum);
                    $("#rtc1").val(json.rlist[2].rtmin);
                    $("#rtc2").val(json.rlist[2].rtmax);
                    $("#rtd").val(json.rlist[3].paynum);
                    $("#rtd1").val(json.rlist[3].rtmin);
                    $("#rtd2").val(json.rlist[3].rtmax);
                }
            })
        });

        function updateRtScale() {
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/web!updateRtScale',
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
    <div class="form-group">
        <label for="rtScale" class="col-sm-2 control-label">返奖比例：</label>
        <div class="col-sm-7">
            <input type="text" class="form-control" id="rtScale" name="rtScale"
                   placeholder="大于0，小于1">
        </div>
        <button class="col-sm-3 btn btn-danger" onclick="updateRtScale()">确定</button>
    </div>
    <div class="form-group">
        <label for="rta" class="col-sm-2 control-label">支付额A：</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="rta" name="rta">
        </div>
        <span  class="col-sm-2 control-label">红包范围：</span>
        <div class="col-sm-3">
            <input type="number" class="form-control"   id="rta1" name="rta1">
        </div>
        <div class="col-sm-3">
            <input type="number" class="form-control"   name="rta2"  id="rta2">
        </div>
    </div>
    <div class="form-group">
        <label for="rtb" class="col-sm-2 control-label">支付额B：</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="rtb" name="rtb">
        </div>
        <span  class="col-sm-2 control-label">红包范围：</span>
        <div class="col-sm-3">
            <input type="number" class="form-control"   id="rtb1"name="rtb1">
        </div>
        <div class="col-sm-3">
            <input type="number" class="form-control"   id="rtb2"name="rtb2">
        </div>
    </div>
    <div class="form-group">
        <label for="rtc" class="col-sm-2 control-label">支付额C：</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="rtc" name="rtc">
        </div>
        <span  class="col-sm-2 control-label">红包范围：</span>
        <div class="col-sm-3">
            <input type="number" class="form-control"   id="rtc1"name="rtc1">
        </div>
        <div class="col-sm-3">
            <input type="number" class="form-control"   id="rtc2" name="rtc2">
        </div>
    </div>
    <div class="form-group">
        <label for="rtd" class="col-sm-2 control-label">支付额D：</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="rtd" name="rtd">
        </div>
        <span  class="col-sm-2 control-label">红包范围：</span>
        <div class="col-sm-3">
            <input type="number" class="form-control"   id="rtd1"name="rtd1">
        </div>
        <div class="col-sm-3">
            <input type="number" class="form-control"   id="rtd2"name="rtd2">
        </div>
    </div>
</form>
</body>
</html>