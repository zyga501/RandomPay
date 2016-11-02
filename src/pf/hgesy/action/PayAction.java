package pf.hgesy.action;

import framework.action.AjaxActionSupport;
import pf.ProjectSettings;
import pf.hgesy.api.DirectPay;
import pf.hgesy.api.RequestBean.DirectPayRequestData;

public class PayAction extends AjaxActionSupport {
    public String directPay() throws Exception {
        DirectPayRequestData directPayRequestData = new DirectPayRequestData();
        directPayRequestData.account = ProjectSettings.getMapData("hgesy").get("account").toString();;
        directPayRequestData.total_fee = "0.10";
        DirectPay directPay = new DirectPay(directPayRequestData);
        directPay.postRequest(ProjectSettings.getMapData("hgesy").get("appKey").toString());
        return AjaxActionComplete();
    }
}
