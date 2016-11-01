package pf.database;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class OrderInfo {
    public static void main(String[] args) throws Exception {
        List<OrderInfo> orderInfoList = OrderInfo.getOrderInfo();
        System.out.println();
    }

    public static List<OrderInfo> getOrderInfo() {
        String statement = "pf.database.mapping.orderInfo.getOrderInfo";
        return Database.Instance().selectList(statement);
    }

    public static List<OrderInfo> getOrderInfoByPara(Map paramap) {
        String statement = "pf.database.mapping.orderInfo.getOrderInfoByPara";
        return Database.Instance().selectList(statement,paramap);
    }

    public static List<OrderInfo> getOrderInfoStatistics(Map paramap) {
        String statement = "pf.database.mapping.orderInfo.getOrderInfoStatistics";
        return Database.Instance().selectList(statement,paramap);
    }
    public static List<OrderInfo> getOrderInfoGroup(OrderInfo orderInfos) {
        String statement = "pf.database.mapping.orderInfo.getOrderInfoGroupByStatus";
        return Database.Instance().selectList(statement, orderInfos);
    }

    public static List<OrderInfo> getOrderInfoGroupByStatusAndCommopenid(OrderInfo orderInfo) {
        String statement = "pf.database.mapping.orderInfo.getOrderInfoGroupByStatusAndCommopenid";
        return Database.Instance().selectList(statement, orderInfo);
    }
    public static boolean updateOrderInfoDone(String commopenid) {
        String statement = "pf.database.mapping.orderInfo.updateOrderInfoDone";
        return Database.Instance().update(statement, commopenid) > 0;
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

    public int getIncome() {
        return income_;
    }

    public void setIncome(int income) {
        income_ = income;
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

    public String getTimeend() {
        return timeend_;
    }

    public void setTimeend(String timeend) {
        this.timeend_ = timeend;
    }

    public Timestamp getCreateTime() {
        return createTime_;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime_ = createTime;
    }

    private long id_;
    private String openid_;
    private int amount_;
    private int bonus_;
    private int comm_;
    private int income_;
    private int status_;
    private String commopenid_;
    private String timeend_;
    private Timestamp createTime_;
}
