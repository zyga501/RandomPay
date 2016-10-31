package pf.hgesy.api;

import pf.hgesy.api.RequestBean.DirectPayRequestData;

public class DirectPay extends HgesyAPIWithSign {
    public final static String DIRECTPAY_API = "http://www.hgesy.com:8080/PayMcc/gateway/direct_pay";

    public DirectPay(DirectPayRequestData directPayRequestData) {
        requestData_ = directPayRequestData;
    }

    @Override
    protected String getAPIUri() {
        return DIRECTPAY_API;
    }
}
