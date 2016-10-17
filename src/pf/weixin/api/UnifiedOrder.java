package pf.weixin.api;

import pf.weixin.api.RequestBean.UnifiedOrderRequestData;

import java.util.Map;

public class UnifiedOrder extends WxAPIWithSign {
    public final static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public UnifiedOrder(UnifiedOrderRequestData unifiedOrderRequestData) {
        requestData_ = unifiedOrderRequestData;
    }

    @Override
    protected String getAPIUri() {
        return UNIFIEDORDER_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        switch (responseResult.get("trade_type").toString()) {
            case "NATIVE": {
                if (responseResult.get("code_url") != null) {
                    return true;
                }
            }
            case "JSAPI": {
                if (responseResult.get("prepay_id") != null) {
                    return true;
                }
            }
        }

        return false;
    }
}
