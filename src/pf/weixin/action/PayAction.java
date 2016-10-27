package pf.weixin.action;

import framework.action.AjaxActionSupport;
import framework.utils.StringUtils;
import pf.ProjectLogger;
import pf.ProjectSettings;
import pf.database.OrderInfo;
import pf.database.PendingOrder;
import pf.database.User;
import pf.utils.BonusPool;
import pf.weixin.api.Mmpaymkttransfers;
import pf.weixin.api.OpenId;
import pf.weixin.api.RequestBean.RandomPayRequestData;
import pf.weixin.api.RequestBean.UnifiedOrderRequestData;
import pf.weixin.api.UnifiedOrder;
import pf.weixin.utils.Signature;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

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
            orderInfo.setStatus(pendingOrder.getStatus());
            OrderInfo.insertOrderInfo(orderInfo);
        }

        pf.database.BonusPool.deleteBonus(new pf.database.BonusPool(pendingOrder.getAmount(), randomPayRequestData.amount));

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
            //if (!getRemortIP(getRequest()).equals("127.0.0.1")) return AjaxActionComplete(false);
            if (getAttribute("userid").equals("")) return AjaxActionComplete(false);
            /*PendingOrder pendingOrder = new PendingOrder();
            pendingOrder.setStatus(Integer.valueOf(getParameter("paystatus").toString()));
            OrderInfo orderInfo =new OrderInfo();
            orderInfo.setStatus(Integer.valueOf(getParameter("paystatus").toString()));
            List<OrderInfo> pendingOrderList = OrderInfo.getOrderInfoGroup(orderInfo);
            for (OrderInfo oi_ : pendingOrderList) {
                RandomPayRequestData randomPayRequestData = new RandomPayRequestData();
                randomPayRequestData.mch_appid = ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
                randomPayRequestData.mchid = ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
                randomPayRequestData.openid = oi_.getCommopenid();
                randomPayRequestData.check_name = "NO_CHECK";
                randomPayRequestData.amount =oi_.getComm();

                randomPayRequestData.desc = "分红入账";
                Mmpaymkttransfers mmpaymkttransfers = new Mmpaymkttransfers(randomPayRequestData, Long.parseLong("1234321"));
                if (!mmpaymkttransfers.postRequest(ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString())) {
                    ProjectLogger.warn("randomPay Failed!");
                    return AjaxActionComplete(false);
                }

                PendingOrder.updatePendingOrderDone(oi_.getCommopenid());
            }*/

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
        unifiedOrderRequestData.appid =ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
        unifiedOrderRequestData.mch_id =ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
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

    public String  makeQcode(){
//        System.out.println(StringUtils.saltDecode("Bb2hFYktzMkNnNlprMWx5VW5PSE9fS003eXREY3c"));
//        setAttribute("wxid",StringUtils.saltEncode("ohEbKs2Cg6Zk1lyUnOHO_KM7ytDc"));
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
            oi.put("Status",(Integer.valueOf(getParameter("paystatus").toString())));
            List<OrderInfo> oList = OrderInfo.getOrderInfoByPara(oi);
            resultMap.put("olist", oList);
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
        Map<String, Object> resultMap = new HashMap<>();
        try {
            OrderInfo oi = new OrderInfo();
            oi.setStatus(0);
            oi.setCommopenid(getAttribute("openid"));
            List<OrderInfo> oList = OrderInfo.getOrderInfoGroupByStatusAndCommopenid(oi);
            if (null!=oList && oList.size()>0)
                resultMap.put("comm", oList.get(0).getComm()/100.00);
            return AjaxActionComplete(true,resultMap);
        }
        catch (Exception e){
            e.printStackTrace();
            return AjaxActionComplete(false);
        }
    }

    public String  signIn___(){
        try{
            User userpara =new User();
            userpara.setUname(getParameter("loginname").toString());
            userpara.setUpwd(getParameter("password").toString());
            User user = User.getUser(userpara);
            if (null!= user){
                setAttribute("userid",user.getId());
                return AjaxActionComplete(true);
            }
        }
        catch (Exception e){
            return AjaxActionComplete(false);
        }
        return AjaxActionComplete(false);
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

    public final static Object syncObject = new Object();
}