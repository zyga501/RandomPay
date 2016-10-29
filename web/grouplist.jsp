<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>审核</title>
  <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/css/animate.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/css/style.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/layer.css" id="layui_layer_skinlayercss">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/layer.ext.css" id="layui_layer_skinlayerextcss">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" id="layui_layer_skinmoonstylecss">
  <style>
    body{ margin:0 auto; text-align:center}
    table{margin:0 auto; width:410px}
    table{ border:1px solid #7b7b81  }
    table tr th{ height:28px; line-height:28px; background:#999}
    table.stripe tr td{ height:28px; line-height:28px; text-align:center;background:#FFF;vertical-align:middle;}
    table.stripe tr.alt td { background:#F2F2F2;}
    table.stripe tr.over td {background: #2789dc;}
    .but {
      -webkit-border-radius: 5px;
      border-radius: 5px;
      background-color: #06af3f;
      color: #FEFEFE;
      border: none;
      font-size: 18px;
      padding: 10px 6px;
    }
  </style>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
  <script type="text/javascript">
    function  tbactive(){
      $(".stripe tr").mouseover(function(){
        //如果鼠标移到class为stripe的表格的tr上时，执行函数
        $(this).addClass("over");}).mouseout(function(){
        //给这行添加class值为over，并且当鼠标一出该行时执行函数
        $(this).removeClass("over");}) //移除该行的class
      $(".stripe tr:even").addClass("alt");
      //给class为stripe的表格的偶数行添加class值为alt
      //www.divcss5.com 整理特效
    };

    var rdlist;
    var nowindex=1;
    function getinfo() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/weixin/Pay!getOrderInfoGroup',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
          if (json.resultCode=="Failed"){
            alert("失败！nwx");
          }else{
            rdlist= json.olist;
            nowindex = 1;
            gopage("pre");
           /* var htmlstr = "总纪录："+json.olist.length+"条<br><table class='stripe'>";
            htmlstr +="<tr><th>账号</th><th>支付金额</th><th>红包</th><th>支付金额</th><th>支付状态</th></tr>";
            for (var j=0;j<json.olist.length;j++){
              htmlstr +="<tr>";
              htmlstr +="<td>****"+(json.olist[j].commopenid).substr(20,4)+"****</td>";
              htmlstr +="<td>"+(json.olist[j].amount)+"</td>";
              htmlstr +="<td>"+(json.olist[j].bonus)+"</td>";
              htmlstr +="<td>"+(json.olist[j].comm)+"</td>";
              var v = json.olist[j].status==0?"未支付":(json.olist[j].status==1?"已支付":"已作废");
              htmlstr +="<td>"+v+"</td>";
              htmlstr +="</tr>";
            } htmlstr +="</table>";
            $("#conetntdiv").html(htmlstr);
            tbactive();*/
          }
        }
      })
    }

    function gopage(objv){
      var htmlstr="";
      if (objv =="pre"){
        if (nowindex>1) nowindex = nowindex -1;else nowindex = 1;
      }
      else if (objv =="next") {
        if (nowindex < Math.ceil(rdlist.length / 20)) nowindex = nowindex + 1; else nowindex = Math.ceil(rdlist.length / 20);
      }
      htmlstr = "纪录："+nowindex+" / "+Math.ceil(rdlist.length/20)+"页<br><table class='stripe'>";
      htmlstr +="<tr><th>账号</th><th>支付金额</th><th>红包</th><th>支付金额</th><th>支付状态</th></tr>";
      for (var j=(nowindex-1)*20;j<Math.min((nowindex)*20, rdlist.length);j++){
        htmlstr +="<tr>";
        htmlstr +="<td>****"+(rdlist[j].commopenid).substr(20,4)+"****</td>";
        htmlstr +="<td>"+(rdlist[j].amount/100.00)+"</td>";
        htmlstr +="<td>"+(rdlist[j].bonus/100.00)+"</td>";
        htmlstr +="<td>"+(rdlist[j].comm/100.00)+"</td>";
        var v = rdlist[j].status==0?"未支付":(rdlist[j].status==1?"已支付":"已作废");
        htmlstr +="<td>"+v+"</td>";
        htmlstr +="</tr>";
      } htmlstr +="</table>";
      $("#conetntdiv").html(htmlstr);
      tbactive();
    }

    function sendpaymsg() {
      $.ajax({
        type: 'post',
        url: '<%=request.getContextPath()%>/weixin/Pay!commPay',
        dataType:"json",
        data:$("form").serialize(),
        success: function (data) {
          var json = eval("(" + data + ")");
            alert(json.resultCode);
          getinfo();
        }
      })
    }
  </script>
</head>
<body>
<form>
  <label>支付状态：</label><select name="paystatus" >
  <option value="0">未支付</option>
  <option value="1">已支付</option>
  <option value="2">已作废</option>
  </select>
  <input type="button" class="btn btn-primary" onclick="getinfo()" value="检 索">
  <input type="button" class="btn btn-danger" onclick="sendpaymsg()" value="确发佣金">
</form>
<div><ul class="pager">
  <li><a href="#" onclick="gopage('pre')">前一页</a></li>
  <li><a href="#" onclick="gopage('next')">后一页</a></li>
</ul></div>
<div id="conetntdiv">
</div>
</body>
</html>