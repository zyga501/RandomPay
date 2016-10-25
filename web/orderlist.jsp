<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
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
    };
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
            var htmlstr = "总纪录："+json.olist.length+"条<br><table class='stripe'>";
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
            tbactive();
          }
        }
      })
    }
  </script>
</head>
<body>
<form>
  <label>返佣状态：</label><select name="paystatus" >
  <option value="0">未返佣</option>
  <option value="1">已返佣</option>
  <option value="2">已作废</option>
  </select>
  <input type="button" class="but" onclick="getinfo()" value="检 索">
</form>
<div id="conetntdiv">
</div>
</body>
</html>