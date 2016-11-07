package pf.weixin.action;

import framework.action.AjaxActionSupport;
import framework.utils.StringUtils;
import pf.ProjectLogger;
import pf.ProjectSettings;
import pf.database.OrderInfo;
import pf.database.PendingOrder;
import pf.hgesy.api.RequestBean.DirectPayRequestData;
import pf.utils.BonusPool;
import pf.weixin.api.Mmpaymkttransfers;
import pf.weixin.api.OpenId;
import pf.weixin.api.RequestBean.RandomPayRequestData;
import pf.weixin.api.RequestBean.UnifiedOrderRequestData;
import pf.weixin.api.UnifiedOrder;
import pf.weixin.utils.Signature;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public void fetchWxCode() throws IOException {
        String appid = ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
        String redirect_uri =  getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/weixin/Pay!fetchWxOpenid";
        String fetchOpenidUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                        "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                appid, redirect_uri, getParameter("redirect_url").toString());
        getResponse().sendRedirect(fetchOpenidUri);
    }

    public void fetchWxOpenid() throws Exception {
        String appid =  ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
        String appsecret =  ProjectSettings.getMapData("weixinserverinfo").get("appsecret").toString();
        OpenId weixinOpenId = new OpenId(appid, appsecret, getParameter("code").toString());
        if (weixinOpenId.getRequest()) {
            setAttribute("openid",weixinOpenId.getOpenId());
            getResponse().sendRedirect(getParameter("state").toString());
        }
    }

    public String mainPage() {
        if (null!=getParameter("tid")) {
            setAttribute("commopenid", StringUtils.saltDecode(getParameter("tid").toString()));
            setParameter("redirect_url","Pay!mainPage?id=1");
        }
        setParameter("redirect_url","Pay!mainPage?id=1");
        if (getParameter("openid")!=null)
            setAttribute("openid",getParameter("openid"));
        if (null == getAttribute("openid") || getAttribute("openid").equals(""))
            return "fetchwxcode";
        return "mainpage";
    }

    public String randomPay() throws Exception {
        //增加 判断如果已经支付 还没领取文字提示红包 返回
        RandomPayRequestData randomPayRequestData = new RandomPayRequestData();
        randomPayRequestData.mch_appid = ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
        randomPayRequestData.mchid =ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
        randomPayRequestData.openid = getAttribute("openid");
        randomPayRequestData.check_name = "NO_CHECK";
        PendingOrder pendingOrder = PendingOrder.getPendingOrderByOpenId(getAttribute("openid"));
        if (pendingOrder != null) {
            randomPayRequestData.amount = BonusPool.getBonus(pendingOrder.getAmount());
        }
        else {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("State", "NoData");
            return AjaxActionComplete(resultMap);
        }

        randomPayRequestData.desc = "红包入账";
        Mmpaymkttransfers mmpaymkttransfers = new Mmpaymkttransfers(randomPayRequestData,Long.parseLong("1234321"));
        if (!mmpaymkttransfers.postRequest( ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString())) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("State", "NoData");
            return AjaxActionComplete(resultMap);
        }

        synchronized (syncObject) {
            PendingOrder.deletePendingOrderByOpenId(getAttribute("openid"));
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOpenid(getAttribute("openid"));
            orderInfo.setAmount(pendingOrder.getAmount());
            orderInfo.setBonus(randomPayRequestData.amount);
            orderInfo.setCommopenid(pendingOrder.getCommopenid());
            orderInfo.setComm(pendingOrder.getComm());
            orderInfo.setIncome((int)(pendingOrder.getAmount() * 0.94) - randomPayRequestData.amount - pendingOrder.getComm());
            orderInfo.setTimeend(pendingOrder.getTimeend());
            orderInfo.setStatus(pendingOrder.getStatus());
            OrderInfo.insertOrderInfo(orderInfo);
        }

        BonusPool.deleteBonus(pendingOrder.getAmount(), randomPayRequestData.amount);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("State", "恭喜您，抽到了" + randomPayRequestData.amount / 100.00 + "元红包！");
        float[] aryint = BonusPool.generateVirtualBonus(Integer.parseInt(getParameter("itempos").toString())-1, (float) (randomPayRequestData.amount / 100.00),200);
        List<Float> hbList = new ArrayList<>();
        for (int i=0;i<aryint.length;i++)
            hbList.add(i,aryint[i]);
        resultMap.put("hblist",hbList);
        return AjaxActionComplete(resultMap);
    }

    public String commPay() throws Exception {
        synchronized (syncObject) {
            if (getAttribute("userid").equals("")) return AjaxActionComplete(false);

            OrderInfo orderInfo =new OrderInfo();
            orderInfo.setStatus(Integer.valueOf(getParameter("paystatus").toString()));
            List<OrderInfo> orderInfoList = OrderInfo.getOrderInfoGroup(orderInfo);
            for (OrderInfo oi_ : orderInfoList) {
                if (oi_.getComm()>=100) {
                    RandomPayRequestData randomPayRequestData = new RandomPayRequestData();
                    randomPayRequestData.mch_appid = ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
                    randomPayRequestData.mchid = ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
                    randomPayRequestData.openid = oi_.getCommopenid();
                    randomPayRequestData.check_name = "NO_CHECK";
                    randomPayRequestData.amount = oi_.getComm();
                    randomPayRequestData.desc = "分红入账";
                    Mmpaymkttransfers mmpaymkttransfers = new Mmpaymkttransfers(randomPayRequestData, Long.parseLong("1234321"));
                    if (!mmpaymkttransfers.postRequest(ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString())) {
                        ProjectLogger.warn("randomPay Failed!");
                        return AjaxActionComplete(false);
                    }
                    OrderInfo.updateOrderInfoDone(oi_.getCommopenid());
                }
            }

            return AjaxActionComplete(true);
        }
    }

    public String checkBonus(){
        PendingOrder pendingOrder = PendingOrder.getPendingOrderByOpenId(getAttribute("openid"));
        return   AjaxActionComplete(pendingOrder!=null);
    }

    public String brandWCPay() throws Exception {
        UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
        unifiedOrderRequestData.appid = ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
        unifiedOrderRequestData.mch_id = ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
        unifiedOrderRequestData.sub_mch_id = "1360239402";//固定死
        unifiedOrderRequestData.body = "购物消费";
        unifiedOrderRequestData.attach = (getAttribute("commopenid"));
        unifiedOrderRequestData.total_fee = (int)(Integer.parseInt(getParameter("total_fee").toString())*(1-0.02+Math.random()*0.04));
        unifiedOrderRequestData.trade_type = "JSAPI";
        unifiedOrderRequestData.openid = getAttribute("openid");
        String requestUrl = getRequest().getRequestURL().toString();
        requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
        requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "weixin/"
                + CallbackAction.BRANDWCPAYCALLBACK;
        unifiedOrderRequestData.notify_url = requestUrl;
        UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
        if (!unifiedOrder.postRequest(ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString())) {
            ProjectLogger.warn("BrandWCPay Failed!");
            return AjaxActionComplete();
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("appId", unifiedOrderRequestData.appid);
        resultMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        resultMap.put("nonceStr", StringUtils.generateRandomString(32));
        resultMap.put("package", "prepay_id=" + unifiedOrder.getResponseResult().get("prepay_id").toString());
        resultMap.put("signType", "MD5");
        resultMap.put("paySign", Signature.generateSign(resultMap,ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString()));
        return AjaxActionComplete(resultMap);
    }

    public void brandWCPay2() throws Exception {
        DirectPayRequestData directPayRequestData = new DirectPayRequestData();
        directPayRequestData.account = ProjectSettings.getMapData("hgesy").get("account").toString();;
        directPayRequestData.total_fee = String.format("%.2f", Double.parseDouble(getParameter("total_fee").toString()) *(1-0.02+Math.random()*0.04) / 100.0);
        directPayRequestData.buildSign(ProjectSettings.getMapData("hgesy").get("appKey").toString());
        PendingOrder pendingOrder = new PendingOrder();
        pendingOrder.setOpenid(getAttribute("openid"));
        pendingOrder.setCommopenid(getAttribute("commopenid"));
        pendingOrder.setOrderNo(directPayRequestData.order_no);
        PendingOrder.insertOrderInfo(pendingOrder);
        String redirectUrl = "http://www.hgesy.com:8080/PayMcc/gateway/direct_pay?";
        redirectUrl += directPayRequestData.buildRequestData();
        getResponse().sendRedirect(redirectUrl);
    }

    public String  makeQcode(){
        setAttribute("wxid",StringUtils.saltEncode(getAttribute("openid")));
        return "promopage";
    }

    private String getRemortIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        System.out.print(ip);
        return ip;
    }

    public String getOrderInfo(){
        // if (!getRemortIP(getRequest()).equals("127.0.0.1")) return AjaxActionComplete(false);
        if (getAttribute("userid").equals("")) return AjaxActionComplete(false);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map oi = new HashMap<>();
            oi.put("status",(Integer.valueOf(getParameter("paystatus").toString())));
            if (null!= getParameter("startdate") && (!getParameter("startdate").toString().equals("")) )
                oi.put("timestart",(getParameter("startdate").toString()));
            if (null!= getParameter("enddate")  && (!getParameter("enddate").toString().equals("")) )
                oi.put("timeend",(getParameter("enddate").toString().concat(" 23:59:59")));
            if (null!= getParameter("openid")  && (!getParameter("openid").toString().equals("")) )
                oi.put("openid",(getParameter("openid").toString()));
            if (null!= getParameter("commopenid")  && (!getParameter("commopenid").toString().equals("")) )
                oi.put("commopenid",(getParameter("commopenid").toString()));
            List<OrderInfo> oList = OrderInfo.getOrderInfoByPara(oi);
            resultMap.put("olist", oList);
            List<OrderInfo> lstatistics = OrderInfo.getOrderInfoStatistics(oi);
            long totalnum =  lstatistics.get(0).getId();
            long bonus =  lstatistics.get(0).getBonus();
            long comm =  lstatistics.get(0).getComm();
            long amount =  lstatistics.get(0).getAmount();
            long income =  amount-bonus-comm;
            resultMap.put("totalnum", totalnum);
            resultMap.put("income", income/100.00);
            resultMap.put("comm", comm/100.00);
            resultMap.put("bonus", bonus/100.00);
            return AjaxActionComplete(true,resultMap);
        }
        catch (Exception e){
            return AjaxActionComplete(false);
        }
    }

    public String getOrderInfoGroup(){
        //if (!getRemortIP(getRequest()).equals("127.0.0.1")) return AjaxActionComplete(false);
        if (getAttribute("userid").equals("")) return AjaxActionComplete(false);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setStatus(Integer.valueOf(getParameter("paystatus").toString()));
            List<OrderInfo> oList = OrderInfo.getOrderInfoGroup(orderInfo);
            resultMap.put("olist", oList);
            return AjaxActionComplete(true,resultMap);
        }
        catch (Exception e){
            e.printStackTrace();
            return AjaxActionComplete(false);
        }
    }
    public String getCommission(){
        // if (!getRemortIP(getRequest()).equals("127.0.0.1")) return AjaxActionComplete(false);
        setAttribute("comm", 0);
        setAttribute("paidcomm", 0);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            OrderInfo oi = new OrderInfo();
            int paidcomm =0;
            //oi.setStatus(0);
            oi.setCommopenid(getAttribute("openid"));
            List<OrderInfo> oList = OrderInfo.getOrderInfoGroupByStatusAndCommopenid(oi);
            if (null!=oList && oList.size()>0)
            for (OrderInfo od:oList){
                if (od.getStatus()==0)
                    setAttribute("comm", od.getComm()/100.00);
                paidcomm +=od.getComm();
            }
            setAttribute("paidcomm",paidcomm/100.00);
            return "infocenterjsp";
        }
        catch (Exception e){
            e.printStackTrace();
            return AjaxActionError();
        }
    }

    public String adminPage(){
        try {
            if (getAttribute("userid").equals(""))
                return "loginpage";
            else
                return "adminpage";
        }
        catch (Exception e){
            return "loginpage";
        }
    }

    public  String getBonusList(){
        Map pa =new HashMap<>();
        pa.put("openid",getAttribute("openid"));//getAttribute("openid")
        List<OrderInfo> ol = OrderInfo.getOrderInfoByPara(pa);
        return AjaxActionComplete(ol);
    }

    public  String getCommList(){
        Map pa =new HashMap<>();
        pa.put("commopenid",getAttribute("openid"));//
        List<OrderInfo> ol = OrderInfo.getOrderInfoByPara(pa);
        return AjaxActionComplete(ol);
    }

    public final static Object syncObject = new Object();
}