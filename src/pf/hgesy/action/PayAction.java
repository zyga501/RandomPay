package pf.hgesy.action;

import framework.action.AjaxActionSupport;
import pf.ProjectSettings;
import pf.hgesy.api.RequestBean.DirectPayRequestData;

public class PayAction extends AjaxActionSupport {
    public void directPay() throws Exception {
        DirectPayRequestData directPayRequestData = new DirectPayRequestData();
        directPayRequestData.account = ProjectSettings.getMapData("hgesy").get("account").toString();;
        directPayRequestData.total_fee = "0.10";
        directPayRequestData.buildSign(ProjectSettings.getMapData("hgesy").get("appKey").toString());
        String redirectUrl = "http://www.hgesy.com:8080/PayMcc/gateway/direct_pay?";
        redirectUrl += directPayRequestData.buildRequestData();
        getResponse().sendRedirect(redirectUrl);
    }
}
