package pf.weixin.action;

import framework.action.AjaxActionSupport;
import pf.ProjectLogger;
import pf.ProjectSettings;
import pf.weixin.api.Mmpaymkttransfers;
import pf.weixin.api.RequestBean.RandomPayRequestData;

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

}