package pf.hgesy.action;

import framework.action.AjaxActionSupport;
import pf.hgesy.api.DirectPay;
import pf.hgesy.api.RequestBean.DirectPayRequestData;

public class PayAction extends AjaxActionSupport {
    public String directPay() throws Exception {
        DirectPayRequestData directPayRequestData = new DirectPayRequestData();
        directPayRequestData.account = "wzqm";
        directPayRequestData.total_fee = 0.01;
        DirectPay directPay = new DirectPay(directPayRequestData);
        directPay.postRequest("A14E9EC817A7A1DE144A0420DCAED3C5");
        return AjaxActionComplete();
    }
}
