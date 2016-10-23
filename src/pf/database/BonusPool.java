package pf.database;

import java.util.List;

public class BonusPool {
    public static void main(String[] args) throws Exception {
        List<BonusPool> bonusPoolList = BonusPool.getBonusByAmount(1000);
        System.out.close();
    }

    public static List<BonusPool> getBonusByAmount(int amount) {
        String statement = "pf.database.mapping.bonusPool.getBonusByAmount";
        return Database.Instance().selectList(statement, amount);
    }

    public static boolean insertBonus(List<BonusPool> bonusPoolList) {
        String statement = "pf.database.mapping.bonusPool.insertBonus";
        return Database.Instance().insert(statement, bonusPoolList) == 1;
    }

    public static boolean deleteBonus(BonusPool bonusPool) {
        String statement = "pf.database.mapping.bonusPool.deleteBonusByAmountAndBouns";
        return Database.Instance().delete(statement, bonusPool) == 1;
    }

    public BonusPool() {

    }

    public BonusPool(int amount, int bonus) {
        amount_ = amount;
        bonus_ = bonus;
    }

    public int getId() {
        return id_;
    }

    public void setId(int id) {
        this.id_ = id;
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

    private int id_;
    private int amount_;
    private int bonus_;
}
