<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <head>
        <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/css/animate.min.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/css/style.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" id="layui_layer_skinmoonstylecss">

        <style>
            body {
                font-size:12px;
            }
            #divparent {
                width: 100%;
                height: 100%;
                background-color: #b5b5c0;
                background-position: center top;
                background-image: url(<%=request.getContextPath()%>/image/top.png);
                background-repeat: no-repeat;
                filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale')";
                -moz-background-size: 100%;
                background-size: 100%;
            }

            #imgdiv {
                position: absolute;
                top: 50%;
                width: 100%;
            }
        </style>
    </head>
<body>
<div id="divparent">
    <div id="imgdiv">
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span12">
                    <table class="table">
                        <thead>
                        <tr>
                            <th><span id="comm"
                                      style="text-align: center">未发佣金:<%=request.getSession().getAttribute("comm")==null?0:request.getSession().getAttribute("comm")%></span>
                            </th>
                            <th><span id="paidcomm"
                                      style="text-align: center">累计佣金:<%=request.getSession().getAttribute("paidcomm")==null?0:request.getSession().getAttribute("paidcomm")%></span>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td colspan=2 style="color: #761c19">红包记录</td>
                        </tr>
                        <tr>
                            <td colspan=2 style="color: #761c19">联系客服</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    </div>
</body>
</html>
