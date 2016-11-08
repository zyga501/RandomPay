<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>扫红码管理平台</title>
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/css/fileinput.css" media="all" rel="stylesheet" type="text/css" />
    <script src="<%=request.getContextPath()%>/js/jquery-2.0.3.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/fileinput.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/fileinput_locale_zh.js" type="text/javascript"></script>
</head>

<body ><br><br><div class="link_code">
    <div id="imgdiv"><img style="width:100px;" src="<%=request.getContextPath()%>/images/linkme.jpg" id="qcodeq">
    </div>
</div>
<form class="form-horizontal" role="form">
<div  >
    <input  style="width:100px;" id="file1" class="file" type="file" data-overwrite-initial="false">
</div></form>
</body>

<script>
    $("#file1").fileinput({
        uploadUrl: 'http:\\192.168.1.68/web!replaceLinkme', // you must set a valid URL here else you will get an error
        allowedFileExtensions : ['jpg', 'png','gif'],
        overwriteInitial: false,
        maxFileSize: 1000,
        maxFilesNum: 1,
        dropZoneEnabled: false,
        slugCallback: function(filename) {
            return filename.replace('(', '_').replace(']', '_');
        }
    });
    $(document).ready(function() {
        $("#file1").fileinput({
            'showPreview' : false,
            'allowedFileExtensions' : ['jpg', 'png','gif'],
            'elErrorContainer': '#errorBlock'
        });
    });
    $("#file1").on("fileuploaded", function(event, data, previewId, index) {
            window.location.reload();
    })
</script>
</html>