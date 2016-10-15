package pf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BonusPool {
    public static void main(String[] args) throws Exception {
        ArrayList<Integer> mapping = new ArrayList<>();
        for (int index = 0; index < 200; ++index) {
            mapping.add(0);
        }

        int totalBonus = 0;
        BonusPool bonusPool = new BonusPool(10, 200, 0.9);
        do {
            int bonus = bonusPool.popBonus();
            totalBonus += bonus;
            mapping.set(bonus - 1, mapping.get(bonus - 1) + 1);
        } while (bonusPool.PoolSize() != 0);

        System.out.println("BonusBase:" + 10 + " BonusMax:" + 200 + " LossRate:" + 0.9 + " PoolSize:" + 10000);
        System.out.println("Total Bonus:" + totalBonus);
        System.out.println("Bonus Distributed:");
        for (int index = 0; index < mapping.size(); ++index) {
            totalBonus += mapping.get(index);
            System.out.println("Bonus " + (index + 1) + " : " + mapping.get(index));
        }
        System.out.println();
    }

    public BonusPool(Integer bonusBase, Integer bonusMax, double lossRate) {
        bonusBase_ = bonusBase;
        bonusMax_ = bonusMax;
        lossRate_ = lossRate;
        poolSize_ = 10000;
        poolLossRate_ = 0.95;
        bonusList_ = new ArrayList<>(poolSize_);
    }

    public BonusPool(Integer bonusBase, Integer bonusMax, double lossRate, Integer poolSize, Double poolLossRate) {
        bonusBase_ = bonusBase;
        bonusMax_ = bonusMax;
        lossRate_ = lossRate;
        poolSize_ = poolSize;
        poolLossRate_ = poolLossRate;
        bonusList_ = new ArrayList<>(poolSize);
    }

    public Integer popBonus() {
        synchronized (bonusList_) {
            if (bonusList_.size() == 0) {
                while (!generateBonus());
            }

            int index = indexRandom_.nextInt(bonusList_.size());
            Integer bonus = bonusList_.get(index);
            bonusList_.remove(index);
            return bonus;
        }
    }

    public int PoolSize() {
        return bonusList_.size();
    }

    private boolean generateBonus() {
        bonusList_.clear();
        int totalBonus = (int)(bonusBase_ * poolSize_ * lossRate_);
        int poolLossSize = (int)(poolSize_ * poolLossRate_);
        int profitSize = poolSize_ - poolLossSize;

        // generate loss bonus
        while (poolLossSize-- != 0) {
            int bonus = (int)(bonusRandom_.nextDouble() * bonusBase_);
            if (bonus == 0) {
                bonus++;
            }
            totalBonus -= bonus;
            bonusList_.add(bonus);
        }

        // generate profit bonus
        int totalProfit = 0;
        List<Integer> profitList = new ArrayList<>();
        while (profitSize-- != 0) {
            int bonus = bonusRandom_.nextInt(bonusMax_ - bonusBase_) + bonusBase_;
            totalProfit += bonus;
            profitList.add(bonus);
        }

        // fixed profit bonus
        int totalError = totalProfit - totalBonus;
        int errorBase = totalError / profitList.size();
        while (totalError > 0) {
            if (profitList.size() <= 0) {
                return false;
            }

            int bonus = Math.min(bonusRandom_.nextInt(errorBase) + errorBase, totalError);
            int index = indexRandom_.nextInt(profitList.size());
            if ((profitList.get(index) - bonus) > bonusMax_) {
                bonus = bonusMax_ - profitList.get(index);
                profitList.remove(index);
                bonusList_.add(bonusMax_);
            }
            else if ((profitList.get(index) - bonus) < bonusBase_) {
                continue;
            }
            else {
                profitList.set(index, profitList.get(index) - bonus);
            }
            totalError -= bonus;
        }

        for (Integer bonus : profitList) {
            bonusList_.add(bonus);
        }

        return true;
    }

    private Integer bonusBase_;
    private Integer bonusMax_;
    private double lossRate_;
    private Integer poolSize_ = 1000;
    private Double poolLossRate_ = 0.95;
    private List<Integer> bonusList_;
    private Random indexRandom_ = new Random();
    private Random bonusRandom_ = new Random();
}
