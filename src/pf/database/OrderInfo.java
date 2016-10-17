package pf.database;

import java.util.List;

public class OrderInfo {
    public static void main(String[] args) throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOpenid("123");
        orderInfo.setAmount(123);
        orderInfo.setBonus(32);
        boolean ret = insertOrderInfo(orderInfo);
    }

    public static List<OrderInfo> getOrderInfo() {
        String statement = "pf.database.mapping.orderInfo.getOrderInfo";
        return Database.Instance().selectList(statement);
    }

    public static boolean insertOrderInfo(OrderInfo orderInfo) {
        String statement = "pf.database.mapping.orderInfo.insertOrderInfo";
        return Database.Instance().insert(statement, orderInfo) == 1;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getOpenid() {
        return openid_;
    }

    public void setOpenid(String openid) {
        this.openid_ = openid;
    }

    public int getAmount() {
        return amount_;
    }

    public void setAmount(int amount) {
        this.amount_ = amount;
    }

    public int getBonus() {
        return bonus_;
    }

    public void setBonus(int bonus) {
        this.bonus_ = bonus;
    }

    private long id_;
    private String openid_;
    private int amount_;
    private int bonus_;
}
