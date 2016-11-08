package pf.hgesy.action;

import framework.action.AjaxActionSupport;
import pf.ProjectSettings;
import pf.database.PayReturn;
import pf.database.PendingOrder;

import java.text.SimpleDateFormat;
import java.util.List;

public class CallbackAction extends AjaxActionSupport {
    public final static String HGESYCALLBACKSUCCESS = "SUCCESS";
    public final static Object syncObject = new Object();

    public void directPay() throws Exception {
        synchronized (syncObject) {
            if (getParameter("order_no") != null) {
                PendingOrder pendingOrder = PendingOrder.getPendingOrderByOrderNo(getParameter("order_no").toString());
                if (pendingOrder != null) {
                    pendingOrder.setAmount((int)(Double.parseDouble(getParameter("total_fee").toString()) * 100));
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
                    pendingOrder.setTimeend((new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" )).format(( new SimpleDateFormat( "yyyyMMddHHmmss" )).parse(getParameter("request_time").toString())));
                    PendingOrder.updatePendingByOrderNo(pendingOrder);
                }
            }
        }

        String requestUrl = getRequest().getRequestURL().toString();
        requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
        requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "weixin/Pay!mainPage";
        if (requestUrl.contains(ProjectSettings.getData("rootUrl").toString())) {
            getResponse().sendRedirect(requestUrl);
        }
        else {
            getResponse().getWriter().write(HGESYCALLBACKSUCCESS);
        }
    }
}
