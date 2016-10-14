<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><%=request.getSession().getAttribute("storename")%> [BestPay]
    </title>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <style type="text/css">
        <!--
        input[type="submit"],input[type="reset"],input[type="button"],button {
            -webkit-appearance: none;
        }

        body {
            -webkit-user-select: none;
            user-select: none;
            background-color: #EFEFEF
        }

        .Layer1 {
            width: 100%;
            z-index: 1;
            top: 28px;
        }

        .STYLE3 {
            color: #FF0000;
            font-size: 22px;
        }

        .STYLE5 {
            color: #FF0000;
            font-size: 16px;
        }

        .STYLE7 {
            font-size: 22px;
        }

        .STYLE8 {
            color: #FF0000;
            font-size: 16px;
            text-align: left;
        }

        .dv1 {
            margin: 12px 1px 12px 1px;
            border: 0px none #d7d7d7;
            padding: 11px 1px;
            font-size: 18px;
            background: #fff;
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            -webkit-box-orient: horizontal;
            -moz-box-orient: horizontal;
            -webkit-flex-direction: row;
            -ms-flex-direction: row;
            flex-direction: row;
            position: relative;
            z-index: 0
        }

        .dv2 {
            padding: 11px 1px;
            text-align: center
        }

        a,input,label {
            outline: 0;
            white-space: nowrap;
        }

        .amount {
            display: block;
            -webkit-box-flex: 1;
            -moz-box-flex: 1;
            -webkit-flex: 1 1 auto;
            -ms-flex: 1 1 auto;
            flex: 1 1 auto;
            z-index: 1;
            padding: 0;
            line-height: 1;
            color: #000;
            text-align: right;
            font-size: 22px;
            white-space: nowrap;
            border-left: 0px;
            border-top: 0px;
            border-right: 0px;
            border-bottom: 0px;
        }

        .amount::before {
            content: '\a5';
            margin-right: .1em
        }

        .but {
            width: 100%;
            -webkit-border-radius: 5px;
            border-radius: 5px;
            background-color: #c8c8c8;
            color: #ffffff;
            border: none;
            font-size: 18px;
            padding: 10px 6px;
        }

        .paynum {
            color: #ff6a60;
            font-size: 20px;
            font-weight: bold;
        }
        -->
    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/bestpay/bestpay.api.js"></script>
    <script language="javascript">

        function h5Pay() {
            $.ajax({
                type: 'post',
                url: '<%=request.getContextPath()%>/bestpay/Pay!orderPay',
                dataType:"json",
                data:$("form").serialize(),
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.resultCode == "Succeed") {
                        var paySuccess = function() {
                            App.exitApp();
                        }
                        var payFail = function() {
                            alert("Fail");
                        }

                        var userAgent = navigator.userAgent || navigator.vendor || window.opera;
                        if (/windows phone/i.test(userAgent)) {
                            alert("Unsupport Windows Phone");
                        }
                        else if (/android/i.test(userAgent)) {
                            Payment.onPay(
                                    {
                                        "MERCHANTID": json.merchantId, //商户号
                                        "SUBMERCHANTID": json.subMerchantId, //子商户号
                                        "ORDERSEQ": json.orderSeq,//订单号
                                        "ORDERAMOUNT": json.orderAmt * 0.01, //订单金额／积分扣减（单位：元，保留小数点后两位）
                                        "ORDERTIME": json.orderReqTime, //订单请求时间格式yyyyMMddHHmmss
                                        "ORDERVALIDITYTIME": "", //订单有效时间
                                        "PRODUCTDESC": json.productDesc, //产品描述
                                        "CUSTOMERID": User.getProduct(), //用户 ID, 在商户系统的登录
                                        "PRODUCTAMOUNT": json.productAmt * 0.01, //产品金额,（单位：元，保留小数点后两位）
                                        "ATTACHAMOUNT": "0.00", //附加金额（单位：元，保留小数点后两位）
                                        "CURTYPE": "RMB", //币种（默认填 RMB ）
                                        "BACKMERCHANTURL": "", //支付结果后台通知地址
                                        "ATTACH": json.attach, //附加信息
                                        "PRODUCTID": "04", //业务标识
                                        "USERIP": "", //用户 IP
                                        "DIVDETAILS": json.divDetail, //分账明细，分账商户必填,
                                        "KEY": json.key, //商户交易KEY
                                        "ACCOUNTID": User.getProduct(), //翼支付账户号
                                        "BUSITYPE": "04", //业务类型
                                        "ORDERREQTRANSEQ": json.orderReqTranSeq, //流水号
                                        "SERVICE": "mobile.security.pay",   //此值写死
                                        "SIGNTYPE": "MD5",
                                        "MAC": json.mac, // MAC 校验
                                        "SIGN": json.sign,
                                        "SUBJECT": "",
                                        "SWTICHACC":"true",
                                        "SESSIONKEY": App.getSessionKey(),
                                        "OTHERFLOW":"false",
                                        "MERCHANTPWD": json.merchantPwd, //商户交易KEY
                                    },,
                                    paySuccess,
                                    payFail);
                        }
                        else if (/iPad|iPhone|iPod/.test(userAgent) && !window.MSStream) {
                            Payment.onPay(
                                    {
                                        "PRODUCTID": "04", //业务标识
                                        "MAC": json.mac, // MAC 校验
                                        "ATTACHAMOUNT": "0.00", //附加金额（单位：元，保留小数点后两位）
                                        "USERIP": "", //用户 IP
                                        "SUBMERCHANTID": json.subMerchantId, //子商户号
                                        "ORDERTIME": json.orderReqTime, //订单请求时间格式yyyyMMddHHmmss
                                        "MERCHANTID": json.merchantId, //商户号
                                        "DIVDETAILS": json.divDetail, //分账明细，分账商户必填,
                                        "ATTACH": json.attach, //附加信息
                                        "BACKMERCHANTURL": json.backMerchantUrl, //支付结果后台通知地址
                                        "PRODUCTAMOUNT": json.productAmt * 0.01, //产品金额,（单位：元，保留小数点后两位）
                                        "CURTYPE": "RMB", //币种（默认填 RMB ）
                                        "BUSITYPE": "09", //业务类型
                                        "PRODUCTDESC": json.productDesc, //产品描述
                                        "CUSTOMERID": "", //用户 ID, 在商户系统的登录
                                        "ORDERVALIDITYTIME": "", //订单有效时间
                                        "ORDERAMOUNT": json.orderAmt * 0.01, //订单金额／积分扣减（单位：元，保留小数点后两位）
                                        "ORDERSEQ": json.orderSeq,//订单号
                                        "MERCHANTPWD": json.merchantPwd, //商户交易KEY
                                        "ACCOUNTID": "", //翼支付账户号
                                        "ORDERREQTRANSEQ": json.orderReqTranSeq, //流水号
                                        "ORDERREQTRNSEQ": json.orderReqTranSeq, //值同上面的ORDERREQTRANSEQ
                                        "SERVICE": "mobile.security.pay",   //此值写死
                                        "SESSIONKEY": App.getSessionKey()
                                    },
                                    paySuccess,
                                    payFail);
                        }
                        else {
                            alert("unknown device!");
                        }
                    }
                }
            })
        }
        function amount(th) {
            var regStrs = [
                ['^0(\\d+)$', '$1'],
                ['[^\\d\\.]+$', ''],
                ['\\.(\\d?)\\.+', '.$1'],
                ['^(\\d+\\.\\d{2}).+', '$1']
            ];
            for (i = 0; i < regStrs.length; i++) {
                var reg = new RegExp(regStrs[i][0]);
                th.value = th.value.replace(reg, regStrs[i][1]);
            }
            $("#productAmt").val(th.value*100);
            if (th.value == "") {
                $("#paynum").text("");
            }
            else {
                $("#paynum").text("￥" + th.value);
            }
            if (th.value != "") {
                $("#butpaynum").css("background-color", "#FF0000");
                $("#butpaynum").removeAttr("disabled");
            }
            else {
                $("#butpaynum").css("background-color", "#c8c8c8");
                $("#butpaynum").attr("disabled", "disabled");
            }
        }
    </script>
</head>

<body>
<input type="hidden" name="hideparam" id="hideparam" value=""/>

<form>
    <input type="hidden" value="${storename}" name="body"/>
    <input type="hidden" value="${id}" name="id"/>
    <input type="hidden" value="" name="productAmt" id="productAmt"/>

    <div class="Layer122">
        <div align="center" class="STYLE3"></div>
        <br>

        <div align="center" class="STYLE3"><img style="width:90px;height:90px;border-radius:8px"
                                                src="<%=request.getContextPath()%>/merchant/SubMerchant!FetchLogo?id=${subMerchantId}"></div>
        <br>

        <div align="center" class="STYLE5"><%=request.getSession().getAttribute("storename")%>
            &nbsp;&nbsp;<%="收银员：" + request.getSession().getAttribute("ucode")%>
        </div>
        <br>
    </div>
    <div class="Layer1">
        <div class="dv1">
	<span class="STYLE7">
  	  <label>消费总额: </label>
  	</span>
            <input type="text" name="paynum" id="paynumbtn"  class="amount" maxlength=10 onkeyup="amount(this)" onchange="amount(this)"  onpaste="return false;"
                   autocomplete="off" placeholder="询问服务员后输入"/>
        </div>
    </div>
    <div class="Layer1">
        <div class="dv2">
	<span class="STYLE7">
  	  <label>实付金额: </label>
  	</span> <label id="paynum" class="paynum"></label><br><br>
            <input type="button" class="but" id="butpaynum"  onclick="h5Pay()" disabled="disabled" value="翼支付"/>
        </div>
    </div>
</form>
</body>
</html>
