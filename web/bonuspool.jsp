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
        url: '<%=request.getContextPath()%>/web!getBonusPoolReturn',
        dataType: "json",
        data: $("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          var tcount =0,tsum=0;
          var htmlstr = "<table class='table table-striped table-bordered table-hover'>";
          htmlstr +="<tr><th>红包类型</th><th>小计金额（元）</th><th>数量（个）</th></tr>";
          for (var j=0;j<json.length;j++){
            htmlstr +="<tr>";
            htmlstr +="<td>"+json[j].amount/100.00+"</td>";
            htmlstr +="<td>"+(json[j].bonussum/100.00)+"</td>";
            htmlstr +="<td>"+(json[j].countnum)+"</td>";
            htmlstr +="</tr>";
            tcount += json[j].countnum;
            tsum += json[j].bonussum;
          } htmlstr +="</table>";
          $("#conetntdiv").html(htmlstr);
          $("#bonusnum").text(tsum/100.00 +"元");
          $("#bonusamount").text(tcount+"个");
        }
      })
    });
  </script>
</head>

<body ><br><br><br><div id="conetntdiv"></div><form class="form-horizontal" role="form">
  <div  class="form-group">
    <label   class="col-sm-4 control-label">红包总额：</label>
    <div class="col-sm-8">
      <span  class="col-sm-4 control-label" id="bonusnum"></span>
    </div>
  </div>
  <div  class="form-group">
    <label   class="col-sm-4 control-label">红包个数：</label>
    <div class="col-sm-8">
      <span  class="col-sm-4 control-label" id="bonusamount"></span>
    </div>
  </div></form>
</body>
</html>