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

    public float getBonus() {
        return bonus_;
    }

    public void setBonus(float bonus) {
        this.bonus_ = bonus;
    }

    public float getComm() {
        return comm_;
    }

    public void setComm(float comm_) {
        this.comm_ = comm_;
    }

    public int getStatus() {
        return status_;
    }

    public void setStatus(int status_) {
        this.status_ = status_;
    }

    private long id_;
    private String openid_;
    private int amount_;
    private float bonus_;
    private float comm_;
    private int status_;
}
