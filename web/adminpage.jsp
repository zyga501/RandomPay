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
    function loaddetail(){
      $("#conetntdiv").load("/orderlist.jsp");
    }
    function loadgroup(){
      $("#conetntdiv").load("/grouplist.jsp");
    }
  </script>
</head>
<body>
<button class="but" onclick="loaddetail()">佣金明细</button>
<button class="but" onclick="loadgroup()">佣金发放审核</button>
<div id="conetntdiv">
</div>
</body>
</html>