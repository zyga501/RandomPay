package pf.database;

import java.util.List;

public class PendingOrder extends OrderInfo {
    public static void main(String[] args) throws Exception {
        PendingOrder pendingOrder = new PendingOrder();
        pendingOrder.setOpenid("123");
        pendingOrder.setAmount(123);
        boolean ret = insertOrderInfo(pendingOrder);
    }

    public static List<PendingOrder> getPendingOrder() {
        String statement = "pf.database.mapping.pendingOrder.getOrderInfo";
        return Database.Instance().selectList(statement);
    }

    public static PendingOrder getPendingOrderByOpenId(String openid) {
        String statement = "pf.database.mapping.pendingOrder.getPendingOrderByOpenid";
        List<PendingOrder> pendingOrders = Database.Instance().selectList(statement, openid);
        if (pendingOrders != null && pendingOrders.size() > 0) {
            return pendingOrders.get(0);
        }
        return null;
    }

    public static boolean deletePendingOrderByOpenId(String openid) {
        String statement = "pf.database.mapping.pendingOrder.deletePendingOrderByOpenid";
        return Database.Instance().delete(statement, openid) == 1;
    }

    public static boolean insertOrderInfo(PendingOrder pendingOrder) {
        String statement = "pf.database.mapping.pendingOrder.insertOrderInfo";
        return Database.Instance().insert(statement, pendingOrder) == 1;
    }

    public static List<PendingOrder> getPendingOrderGroup(int status) {
        String statement = "pf.database.mapping.pendingOrder.getOrderInfoGroupByStatus";
        return Database.Instance().selectList(statement, status);
    }

    public static boolean updatePendingOrderDone(String commopenid) {
        String statement = "pf.database.mapping.pendingOrder.updateOrderInfoDone";
        return Database.Instance().update(statement, commopenid) > 0;
    }
}
