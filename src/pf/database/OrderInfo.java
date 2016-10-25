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

    public static List<OrderInfo> getOrderInfo(pf.database.OrderInfo rtstatus) {
        String statement = "pf.database.mapping.orderInfo.getOrderInfoByStatus";
        return Database.Instance().selectList(statement,rtstatus);
    }

    public static List<OrderInfo> getOrderInfoGroup(pf.database.OrderInfo rtstatus) {
        String statement = "pf.database.mapping.orderInfo.getOrderInfoGroupByStatus";
        return Database.Instance().selectList(statement,rtstatus);
    }

    public static boolean updateOrderInfoDone(OrderInfo orderInfo) {
        String statement = "pf.database.mapping.orderInfo.updateOrderInfoDone";
        return Database.Instance().insert(statement, orderInfo) > 0;
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

    public int getComm() {
        return comm_;
    }

    public void setComm(int comm_) {
        this.comm_ = comm_;
    }

    public int getStatus() {
        return status_;
    }

    public void setStatus(int status_) {
        this.status_ = status_;
    }

    public String getCommopenid() {
        return commopenid_;
    }

    public void setCommopenid(String commopenid) {
        this.commopenid_ = commopenid;
    }

    private long id_;
    private String openid_;
    private int amount_;
    private int bonus_;
    private int comm_;
    private int status_;
    private String commopenid_;
}
