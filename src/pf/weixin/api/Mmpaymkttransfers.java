package pf.weixin.api;

import org.xml.sax.SAXException;
import pf.ProjectLogger;
import pf.weixin.api.RequestBean.RandomPayRequestData;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

public class Mmpaymkttransfers extends WeixinAPIWithSignEx {
    public final static String TRANSFERS_API = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    public Mmpaymkttransfers(RandomPayRequestData randomPayRequestData, long createUser) {
        requestData_ = randomPayRequestData;
        createUser_ = createUser;
    }

    @Override
    protected String getAPIUri() {
        return TRANSFERS_API;
    }

    @Override
    public boolean postRequest(String apiKey) throws Exception {
        apiKey_ = apiKey;
        return super.postRequest(apiKey);
    }

    protected boolean handlerResponse(Map<String,Object> responseResult) throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        try {
            String returnCode = responseResult.get("return_code").toString().toUpperCase();
            String resultCode = responseResult.get("result_code").toString().toUpperCase();
            RandomPayRequestData randomPayRequestData = (RandomPayRequestData)requestData_;
            if (returnCode.compareTo("SUCCESS") == 0) {
                if (resultCode.compareTo("SUCCESS") == 0) {
                    saveOrderToDb(responseResult);
                    return true;
                }
                else {
                    String errorCode = responseResult.get("err_code").toString().toUpperCase();
                    ProjectLogger.warn(String.format("Mmpaymkttransfers UnHandler Exception, errorCode:%s", errorCode));
                }
            }
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) {
//        WxOrderInfo orderInfo = new WxOrderInfo();
//        orderInfo.setAppid(responseResult.get("appid").toString());
//        orderInfo.setMchId(responseResult.get("mch_id").toString());
//        orderInfo.setSubMchId(responseResult.get("sub_mch_id").toString());
//        JSONObject jsonObject = JSONObject.fromObject(responseResult.get("attach").toString());
//        if (jsonObject.get("body") != null) {
//            orderInfo.setBody(jsonObject.get("body").toString());
//        }
//        else {
//            orderInfo.setBody(responseResult.get("attach").toString());
//        }
//        orderInfo.setTransactionId(responseResult.get("transaction_id").toString());
//        orderInfo.setOutTradeNo(responseResult.get("out_trade_no").toString());
//        orderInfo.setBankType(responseResult.get("bank_type").toString());
//        orderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
//        orderInfo.setTimeEnd(responseResult.get("time_end").toString());
//        orderInfo.setOpenId(responseResult.get("openid").toString());
//        orderInfo.setCreateUser(createUser_);
//        return WxOrderInfo.insertOrderInfo(orderInfo);
        System.out.println(responseResult.size());
        return true;
    }

    private long createUser_;
    private String apiKey_;
}
