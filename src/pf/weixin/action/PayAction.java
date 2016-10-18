package pf.weixin.action;

import framework.action.AjaxActionSupport;
import framework.utils.SessionCache;
import framework.utils.StringUtils;
import framework.utils.Zip;
import net.sf.json.JSONObject;
import pf.ProjectLogger;
import pf.ProjectSettings;
import pf.database.OrderInfo;
import pf.database.PendingOrder;
import pf.utils.BonusPool;
import pf.weixin.api.Mmpaymkttransfers;
import pf.weixin.api.OpenId;
import pf.weixin.api.RequestBean.RandomPayRequestData;
import pf.weixin.api.RequestBean.UnifiedOrderRequestData;
import pf.weixin.api.UnifiedOrder;
import pf.weixin.utils.Signature;

import java.io.IOException;
import java.util.HashMap;
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
            getResponse().sendRedirect(getParameter("state").toString() + "&openid=" + weixinOpenId.getOpenId());
        }
    }

    public String mainPage() {
        setParameter("redirect_url","Pay!mainPage?id=1");
        if (getParameter("openid")!=null)
            setAttribute("openid",getParameter("openid"));
        if (null == getAttribute("openid") || getAttribute("openid").equals(""))
            return "fetchwxcode";
        return "mainpage";
    }

    public String randomPay() throws Exception {
        RandomPayRequestData randomPayRequestData = new RandomPayRequestData();
        randomPayRequestData.mch_appid = ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
        randomPayRequestData.mchid =ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
        randomPayRequestData.openid = getAttribute("openid");
        randomPayRequestData.check_name = "NO_CHECK";
        PendingOrder pendingOrder = PendingOrder.getPendingOrderByOpenId(getAttribute("openid"));
        if (pendingOrder != null) {
            randomPayRequestData.amount = BonusPool.getBonus(pendingOrder.getAmount() / 100) * 100;
            ProjectLogger.info("Bonus:" + randomPayRequestData.amount);
        }
        else {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("State", "NoData");
            return AjaxActionComplete(resultMap);
        }

        randomPayRequestData.desc = "红包入账";
        Mmpaymkttransfers mmpaymkttransfers = new Mmpaymkttransfers(randomPayRequestData,Long.parseLong("1234321"));
        if (!mmpaymkttransfers.postRequest( ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString())) {
            ProjectLogger.warn("randomPay Failed!");
            return AjaxActionComplete();
        }

        PendingOrder.deletePendingOrderByOpenId(getAttribute("openid"));
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOpenid(getAttribute("openid"));
        orderInfo.setAmount(pendingOrder.getAmount());
        orderInfo.setBonus(randomPayRequestData.amount);
        OrderInfo.insertOrderInfo(orderInfo);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("State", "恭喜您，抽到了" + randomPayRequestData.amount / 100 + "元红包！");
        return AjaxActionComplete(resultMap);
    }

    public String brandWCPay() throws Exception {
        System.out.println("total_fee="+ getParameter("total_fee"));
        UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
        unifiedOrderRequestData.appid =ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
        unifiedOrderRequestData.mch_id =ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
        unifiedOrderRequestData.sub_mch_id = "1360239402";//固定死
        unifiedOrderRequestData.body = "body";
        unifiedOrderRequestData.attach = "none";
        unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
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

}