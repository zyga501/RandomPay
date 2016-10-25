package pf.weixin.action;

import framework.action.AjaxActionSupport;
import framework.utils.XMLParser;
import pf.ProjectSettings;
import pf.database.OrderInfo;
import pf.database.PendingOrder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String BRANDWCPAYCALLBACK = "Callback!brandWCPay";
    public final static String WEIXINCALLBACKSUCCESS = "" +
            "<xml>\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";
    public final static Object syncObject = new Object();

    public void brandWCPay() throws Exception {
        handlerCallback();
        getResponse().getWriter().write(WEIXINCALLBACKSUCCESS);
    }

    private void handlerCallback() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }
        bufferedReader.close();

        String responseString = stringBuilder.toString();
        Map<String,Object> responseResult = XMLParser.convertMapFromXml(responseString);
        saveOrderToDb(responseResult);
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) {
        synchronized (syncObject) {
            if (PendingOrder.getPendingOrderByOpenId(responseResult.get("openid").toString()) == null) {
                PendingOrder pendingOrder = new PendingOrder();
                pendingOrder.setOpenid(responseResult.get("openid").toString());
                pendingOrder.setAmount(Integer.parseInt(responseResult.get("total_fee").toString()));
                PendingOrder.insertOrderInfo(pendingOrder);
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOpenid(responseResult.get("openid").toString());
                orderInfo.setAmount(pendingOrder.getAmount());
                orderInfo.setBonus(pendingOrder.getBonus());
                orderInfo.setComm((int)(pendingOrder.getAmount()* (Float.parseFloat(ProjectSettings.getData("commrate").toString()))));
                orderInfo.setCommopenid(responseResult.get("attach").toString());
                OrderInfo.insertOrderInfo(orderInfo);
            }
        }
        return true;
    }
}