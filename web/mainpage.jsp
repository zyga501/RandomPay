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
</head>

<body class="fixed-sidebar full-height-layout gray-bg  pace-done skin-1" style="overflow:hidden">
<div class="pace  pace-inactive">
    <div class="pace-progress" data-progress-text="100%" data-progress="99" style="width: 100%;">
        <div class="pace-progress-inner"></div>
    </div>
    <div class="pace-activity"></div>
</div>
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="nav-close"><i class="fa fa-times-circle"></i>
        </div>
        <div class="slimScrollDiv" style="position: relative; width: auto; height: 100%;">
            <div class="sidebar-collapse" style="width: auto; height: 100%;">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <span><img alt="image" class="img-circle" src="img/userlogo.jpg" style="width: 80px"></span>
                            <a data-toggle="dropdown" class="dropdown-toggle"
                               href="<%=request.getContextPath()%>/#">
                                <span class="clear">
                               <span class="block m-t-xs"><strong class="font-bold">${role}</strong></span>
                                <span class="text-muted text-xs block">${unick}<b class="caret"></b></span>
                                </span>
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                <li><a class="J_menuItem" href="<%=request.getContextPath()%>/form_avatar.html"
                                       data-index="0">修改头像</a>
                                </li>
                                <li><a class="J_menuItem" href="<%=request.getContextPath()%>/profile.html"
                                       data-index="1">个人资料</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="<%=request.getContextPath()%>/web!Logout">安全退出</a>
                                </li>
                            </ul>
                        </div>
                        <div class="logo-element"><img alt="image" class="img-circle" src="img/userlogo.jpg" style="width: 60px">
                        </div>
                    </li>
                    <s:iterator value="menulist" id="ml" status="st">
                        <li>
                            <a href="https://www.baidu.com/s?wd=<s:property value="#ml.prenode.menuname"></s:property>">
                                <i class="fa <s:property value="#ml.prenode.labelico"></s:property>"></i>
                                <span class="nav-label"><s:property value="#ml.prenode.menuname"></s:property></span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level collapse">
                                <s:iterator value="#ml.subnode" id="map">
                                    <li>
                                        <a class="J_menuItem" href="<s:property value="#map.webpath"></s:property>"
                                           data-index="0"><s:property value="#map.menuname"></s:property></a>
                                    </li>
                                </s:iterator>
                            </ul>
                        </li>
                    </s:iterator>
                </ul>

            </div>
            <div class="slimScrollBar"
                 style="width: 4px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 655.179px; background: rgb(0, 0, 0);"></div>
            <div class="slimScrollRail"
                 style="width: 4px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; opacity: 0.9; z-index: 90; right: 1px; background: rgb(51, 51, 51);"></div>
        </div>
    </nav>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row content-tabs">
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
            </button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <a href="javascript:;" class="active J_menuTab" data-id="index_v1.html">首页</a>
                </div>
            </nav>
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
            </button>
            <div class="btn-group roll-nav roll-right">
                <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                </button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <li class="J_tabShowActive"><a>定位当前选项卡</a>
                    </li>
                    <li class="divider"></li>
                    <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                    </li>
                    <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                    </li>
                </ul>
            </div>
            <a href="<%=request.getContextPath()%>/web!Logout" class="roll-nav roll-right J_tabExit"><i
                    class="fa fa fa-sign-out"></i> 退出</a>
        </div>
        <div class="row J_mainContent" id="content-main">
            <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="welcome.html" frameborder="0"
                    data-id="index_v1.html" seamless=""></iframe>
        </div>
    </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.metisMenu.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/layer.js"></script>
<script src="js/hplus.min.js"></script>
<script type="text/javascript" src="js/contabs.min.js"></script>
<script src="js/pace.min.js"></script>
<div id="com-d-top-dv"
     style="position:fixed;z-index:9999;width:auto;left:0px;top:0px;border:0px ;align:center">
    <a class="navbar-minimalize  btn btn-primary " href="#"><i class="fa fa-bars"></i></a>
</div>

</body>
</html>