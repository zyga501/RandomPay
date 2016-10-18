package pf.weixin.action;

import framework.action.AjaxActionSupport;
import framework.utils.SessionCache;
import framework.utils.StringUtils;
import framework.utils.Zip;
import net.sf.json.JSONObject;
import pf.ProjectLogger;
import pf.ProjectSettings;
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
            randomPayRequestData.openid = "o8Dbet8_qWwa7qCOJiBgAFswd9e4";
            randomPayRequestData.check_name = "NO_CHECK";
            randomPayRequestData.amount = (int)Double.parseDouble(getParameter("total_fee").toString());
            randomPayRequestData.desc = "零钱入账";
            Mmpaymkttransfers mmpaymkttransfers = new Mmpaymkttransfers(randomPayRequestData,Long.parseLong("1234321"));
            if (!mmpaymkttransfers.postRequest( ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString())) {
                ProjectLogger.warn("randomPay Failed!");
                return AjaxActionComplete();
            }

        return AjaxActionComplete(true);
    }

    public String brandWCPay() throws Exception {
        System.out.println("234234");
        System.out.println("openid="+ getAttribute("openid"));
        System.out.println("total_fee="+ getAttribute("total_fee"));
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
            if (null!=getParameter("out_trade_no")) {
                unifiedOrderRequestData.out_trade_no = getParameter("out_trade_no").toString();
            }

            UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
            if (!unifiedOrder.postRequest(ProjectSettings.getMapData("weixinserverinfo").get("apikey").toString())) {
                ProjectLogger.warn("BrandWCPay Failed!");
                return AjaxActionComplete();
            }
        return AjaxActionComplete(true);
    }

}