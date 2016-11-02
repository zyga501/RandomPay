package pf.weixin.action;

import framework.action.AjaxActionSupport;
import framework.utils.XMLParser;
import pf.database.PayReturn;
import pf.database.PendingOrder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        System.out.println(responseString);
        Map<String,Object> responseResult = XMLParser.convertMapFromXml(responseString);
        saveOrderToDb(responseResult);
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) throws ParseException {
        synchronized (syncObject) {
            if (PendingOrder.getPendingOrderByOpenId(responseResult.get("openid").toString()) == null) {
                PendingOrder pendingOrder = new PendingOrder();
                pendingOrder.setOpenid(responseResult.get("openid").toString());
                pendingOrder.setAmount(Integer.parseInt(responseResult.get("total_fee").toString()));
                pendingOrder.setCommopenid(responseResult.get("attach").toString());
                List<PayReturn> payReturnList = PayReturn.getPayReturn();
                if (payReturnList.size() > 0) {
                    int amountArray[] = {1000, 2000, 5000, 10000};
                    int minIndex = 0;
                    int minValue = 100000;
                    for (int index = 0; index < amountArray.length; index++) {
                        if (Math.abs(pendingOrder.getAmount() - amountArray[index]) < minValue) {
                            minIndex = index;
                            minValue = Math.abs(pendingOrder.getAmount() - amountArray[index]);
                        }
                    }
                    pendingOrder.setComm((int)(pendingOrder.getAmount()* payReturnList.get(minIndex).getCommrate()));
                }
                pendingOrder.setTimeend((new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" )).format(( new SimpleDateFormat( "yyyyMMddHHmmss" )).parse(responseResult.get("time_end").toString())));
                PendingOrder.insertOrderInfo(pendingOrder);
            }
        }
        return true;
    }
}