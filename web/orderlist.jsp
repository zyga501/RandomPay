<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>

  <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/css/animate.min.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/css/style.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/layer.css" id="layui_layer_skinlayercss">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/layer.ext.css" id="layui_layer_skinlayerextcss">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" id="layui_layer_skinmoonstylecss">
  <link href="<%=request.getContextPath()%>/css/laypage.css" rel="stylesheet" type="text/css"/>
  <link href="<%=request.getContextPath()%>/css/laydate.css" rel="stylesheet" type="text/css"/>
  <link href="<%=request.getContextPath()%>/css/danlanlaydate.css" rel="stylesheet" type="text/css"/>
  <style>
    body{ margin:0 auto; text-align:center}
    table{margin:0 auto; width:410px}
    table{ border:1px solid #7b7b81  }
    table tr th{ height:28px; line-height:28px; background:#999}
    table.stripe tr td{ height:28px; line-height:28px; text-align:center;background:#FFF;vertical-align:middle;}
    table.stripe tr.alt td { background:#F2F2F2;}
    table.stripe tr.over td {background: #2789dc;}
    .but {
      -webkit-border-radius: 3px;
      border-radius: 3px;
      background-color: #06af3f;
      color: #FEFEFE;
      border: none;
      font-size: 18px;
      padding: 6px 6px;
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
        url: '<%=request.getContextPath()%>/weixin/Pay!getOrderInfo',
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
            /*var htmlstr = "总纪录："+Math.ceil(json.olist.length/20)+"页<br><table class='stripe'>";
            htmlstr +="<tr><th>账号</th><th>支付金额</th><th>红包</th><th>返佣金额</th><th>返佣状态</th></tr>";
            for (var j=0;j<json.olist.length;j++){
              htmlstr +="<tr>";
              htmlstr +="<td>****"+(json.olist[j].openid).substr(20,4)+"****</td>";
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
        htmlstr +="<tr><th>账号</th><th>支付金额</th><th>红包</th><th>返佣金额</th><th>返佣状态</th></tr>";
        for (var j=(nowindex-1)*20;j<Math.min((nowindex)*20, rdlist.length);j++){
          htmlstr +="<tr>";
          htmlstr +="<td>****"+(rdlist[j].openid).substr(20,4)+"****</td>";
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
  </script>
</head>
<body>
<center>
  <div class="btn-group form-inline col-lg-12">
    <button type="button" class="btn btn-warning btn-xs" onclick="setdate('today')">今天</button>
    <button type="button" class="btn btn-default btn-xs" onclick="setdate('yestoday')">昨天</button>
    <button type="button" class="btn btn-default btn-xs" onclick="setdate('thisweek')">本周</button>
    <button type="button" class="btn btn-default btn-xs" onclick="setdate('preweek')">上周</button>
    <button type="button" class="btn btn-default btn-xs" onclick="setdate('thismonth')">本月</button>
    <button type="button" class="btn btn-default btn-xs" onclick="setdate('premonth')">上月</button>
    <button type="button" class="btn btn-danger btn-xs" onclick="exportexcel()">导出</button>
  </div>
</center>
<form id="searchform">
  <div class="container">
    <div class="row">
      <div class="col-md-4">
        <input name="startdate" id="startdate" class="form-control layer-date" placeholder="开始日期"
               onclick="laydate({istime: true, format: 'YYYY-MM-DD'})"></div>
      <div class="col-md-4">
        <input name="enddate" id="enddate" class="form-control layer-date" placeholder="结束日期"
               onclick="laydate({istime: true, format: 'YYYY-MM-DD'})"></div>
      <div class="col-md-2"><label>返佣状态：</label><select name="paystatus" >
        <option value="0">未返佣</option>
        <option value="1">已返佣</option>
        <option value="2">已作废</option>
      </select></div>
      <div class="col-md-1"><input  type="reset" class=" btn btn-danger" value="重置"></div>
      <div class="col-md-1">
        <input type="button" class="btn btn-primary" onclick="getinfo()" value="检 索"></div>
      </div>
    <div class="row">
      <div class="col-md-4"><input name="submchid" type="text" class="form-control" placeholder="消费号"></div>
      <div class="col-md-4"><input name="amount" type="text" class="form-control" placeholder="金额(如=10 >10 <10)"></div>
      <div class="col-md-4"><input name="uname" type="text" class="form-control" placeholder="代理/销售编号"></div>
  </div>
  </div>

</form>
<div><ul class="pager">
  <li><a href="#" onclick="gopage('pre')">前一页</a></li>
  <li><a href="#" onclick="gopage('next')">后一页</a></li>
</ul></div>
<div id="conetntdiv">
</div>
</body>

<script src="<%=request.getContextPath()%>/js/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/laypage.js"></script>
<script src="<%=request.getContextPath()%>/js/laydate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dateRangeUtil.js"></script>
<script>
  $().ready(function(){
    setdate('today');
  });

  function exportexcel(){
    window.location.href="<%=request.getContextPath()%>/weixin/wx!Exportdetail?"
            + encodeURI(encodeURI(decodeURIComponent($("#searchform").serialize(),true)));
  }

  function setdate(varstr) {
    if (varstr == "today") {
      $("#startdate").val(DateUtil.Format(dateRangeUtil.getCurrentDate()));
      $("#enddate").val(DateUtil.Format(dateRangeUtil.getCurrentDate()));
    } else if (varstr == "yestoday") {
      $("#startdate").val(DateUtil.Format(dateRangeUtil.getPreviousDate()));
      $("#enddate").val(DateUtil.Format(dateRangeUtil.getPreviousDate()));
    } else if (varstr == "thisweek") {
      $("#startdate").val(DateUtil.Format(dateRangeUtil.getCurrentWeek()[0]));
      $("#enddate").val(DateUtil.Format(dateRangeUtil.getCurrentWeek()[1]));
    } else if (varstr == "preweek") {
      $("#startdate").val(DateUtil.Format(dateRangeUtil.getPreviousWeek()[0]));
      $("#enddate").val(DateUtil.Format(dateRangeUtil.getPreviousWeek()[1]));
    } else if (varstr == "thismonth") {
      $("#startdate").val(DateUtil.Format(dateRangeUtil.getCurrentMonth()[0]));
      $("#enddate").val(DateUtil.Format(dateRangeUtil.getCurrentMonth()[1]));
    } else if (varstr == "premonth") {
      $("#startdate").val(DateUtil.Format(dateRangeUtil.getPreviousMonth()[0]));
      $("#enddate").val(DateUtil.Format(dateRangeUtil.getPreviousMonth()[1]));
    }
  }
  function searchlist(curr) {
    $("#pagecountdiv").html("");
    $("#contentdiv").html("<img  src='<%=request.getContextPath()%>/img/loading.gif'>");
    $.ajax({
      type: 'post',
      url: '<%=request.getContextPath()%>/weixin/wx!Fetchdetail',
      dataType: "json",
      data: $("#searchform").serialize() + "&currpagenum=" + curr,
      success: function (data) {
        var json = eval("(" + data + ")");
        if (json.errorMessage != null) {
          $("#navigatediv").html("");
          $("#contentdiv").html(json.errorMessage);
        }
        else {
          laypage({
            cont: 'navigatediv',
            pages: json[0].pagecount,
            skip: true,
            skin: 'yahei',
            jump: function (obj, first) {
              if(!first){
                $("#contentdiv").html("<img  src='<%=request.getContextPath()%>/img/loading.gif'>");
                $.ajax({
                  type: 'post',
                  url: '<%=request.getContextPath()%>/weixin/wx!Fetchdetail',
                  dataType: "json",
                  data: $("#searchform").serialize() + "&currpagenum=" + obj.curr,
                  success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.errorMessage != null) {
                      $("#navigatediv").html("");
                      $("#contentdiv").html(json.errorMessage);
                    }
                    else {
                      $("#pagecountdiv").html("<span class='label label-success'>" +
                              "总共数据<span class='badge'>" + json[0]["totalcount"] + " </span>"+
                              "交易额<span class='badge'>￥" + json[0]["totalsum"] + " </span>"+
                              "手续费<span class='badge'>￥" + json[0]["ratefee"] + " </span></span>");
                      var htmlStr = "<table class='table table-striped table-bordered table-hover'>" + "<thead>";
                      for (var key in json[1]) {
                        htmlStr += "<th>" + key + "</th>";
                      }
                      htmlStr += "</thead><tbody>";
                      for (var i = 1, l = json.length; i < l; i++) {
                        htmlStr += "<tr>";
                        for (var key in json[i]) {
                          htmlStr += "<td>" + json[i][key] + "</td>";
                        }
                        htmlStr += "</tr>";
                      }
                      htmlStr += "</tobdy></table>";
                      $("#contentdiv").html(htmlStr);
                    }
                  }
                })}
            }
          })
          $("#pagecountdiv").html("<span class='label label-success'>" +
                  "总共数据<span class='badge'>" + json[0]["totalcount"] + " </span>"+
                  "交易额<span class='badge'>￥" + json[0]["totalsum"] + " </span>"+
                  "手续费<span class='badge'>￥" + json[0]["ratefee"] + " </span></span>");
          var htmlStr = "<table class='table table-striped table-bordered table-hover'>" + "<thead>";
          for (var key in json[1]) {
            htmlStr += "<th>" + key + "</th>";
          }
          htmlStr += "</thead><tbody>";
          for (var i = 1, l = json.length; i < l; i++) {
            htmlStr += "<tr>";
            for (var key in json[i]) {
              htmlStr += "<td>" + json[i][key] + "</td>";
            }
            htmlStr += "</tr>";
          }
          htmlStr += "</tobdy></table>";
          $("#contentdiv").html(htmlStr);
        }
      }
    })
  }
</script>
</html>