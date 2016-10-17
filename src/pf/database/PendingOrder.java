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

    public static PendingOrder getPendingOrderByOpenId() {
        String statement = "pf.database.mapping.pendingOrder.getPendingOrderByOpenid";
        return Database.Instance().selectOne(statement);
    }

    public static boolean insertOrderInfo(PendingOrder pendingOrder) {
        String statement = "pf.database.mapping.pendingOrder.insertOrderInfo";
        return Database.Instance().insert(statement, pendingOrder) == 1;
    }
}
