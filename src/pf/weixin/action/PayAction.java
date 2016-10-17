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

    public void jsPay() throws IOException {
        do {
            String appid = ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
            String subMerchantUserId = "0";// getParameter("id").toString();
            String redirect_uri = getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/weixin/jsPayCallback.jsp";
            String sessionId = getRequest().getSession().getId();
            String jspayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                            "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                    appid, redirect_uri, sessionId);
            // save session data, state is too short
            String data = String.format("{'id':'%s','body':'%s','fee':'%s','no':'%s','url':'%s','data':'%s'}",
                    subMerchantUserId,
                    StringUtils.convertNullableString(""),
                    StringUtils.convertNullableString(getParameter("total_fee")),
                    StringUtils.convertNullableString(""),//getParameter("out_trade_no")
                    StringUtils.convertNullableString(null),
                    StringUtils.convertNullableString(null));
            String zipData = Zip.zip(data);
            getRequest().getSession().setAttribute("data", zipData);
            SessionCache.setSessionData(sessionId, zipData);
            getResponse().sendRedirect(jspayUri);
        } while (false);
    }

    public String brandWCPay() throws Exception {
        // get session data and remove data
        JSONObject jsonObject = null;
        if (!StringUtils.convertNullableString(getParameter("data")).isEmpty()) {
            jsonObject = JSONObject.fromObject(Zip.unZip(getParameter("data").toString()));
        }

        String sessionId = StringUtils.convertNullableString(getParameter("state"));
        if (jsonObject == null && !sessionId.isEmpty()) {
            String sesseionData = SessionCache.getSessionData(sessionId).toString();
            if (!sesseionData.isEmpty()) {
                jsonObject = JSONObject.fromObject(Zip.unZip(sesseionData));
            }
        }

        getRequest().getSession().removeAttribute("data");
        if (!sessionId.isEmpty())
            SessionCache.clearSessionData(sessionId);

        if (jsonObject == null) {
            ProjectLogger.warn("BrandWCPay Failed! Session Data Is Miss!");
            return AjaxActionComplete(false);
        }

        String subMerchantUserId = jsonObject.get("id").toString();
        String body = jsonObject.get("body").toString();
        int total_fee = (int)Double.parseDouble(jsonObject.get("fee").toString());
        String out_trade_no = jsonObject.get("no").toString();
        String redirect_uri = jsonObject.get("url").toString();
        String data = jsonObject.get("data").toString();
        String code = getParameter("code").toString();

        if (subMerchantUserId.isEmpty() || code.isEmpty()) {//1360239402
            return AjaxActionComplete();
        }

            UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
            unifiedOrderRequestData.appid =ProjectSettings.getMapData("weixinserverinfo").get("appid").toString();
            unifiedOrderRequestData.mch_id =ProjectSettings.getMapData("weixinserverinfo").get("mchid").toString();
            unifiedOrderRequestData.sub_mch_id = "1360239402";//固定死
            unifiedOrderRequestData.body = body;
            unifiedOrderRequestData.attach = String.format("{'id':'%s','body':'%s','redirect_uri':'%s','data':'%s'}",
                    subMerchantUserId, unifiedOrderRequestData.body, redirect_uri, data);
            unifiedOrderRequestData.total_fee = total_fee;
            unifiedOrderRequestData.trade_type = "JSAPI";
            OpenId openId = new OpenId(ProjectSettings.getMapData("weixinserverinfo").get("appid").toString(),
                    ProjectSettings.getMapData("weixinserverinfo").get("secret").toString(), code);
            if (openId.getRequest()) {
                unifiedOrderRequestData.openid = openId.getOpenId();
            }
            else {
                return AjaxActionComplete();
            }
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "weixin/"
                    + CallbackAction.BRANDWCPAYCALLBACK;
            unifiedOrderRequestData.notify_url = requestUrl;
            if (!out_trade_no.isEmpty()) {
                unifiedOrderRequestData.out_trade_no = out_trade_no;
            }

            UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
            if (!unifiedOrder.postRequest(ProjectSettings.getMapData("weixinserverinfo").get("appid").toString())) {
                ProjectLogger.warn("BrandWCPay Failed!");
                return AjaxActionComplete();
            }
        return AjaxActionComplete(true);
    }

}