package pf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BonusPool {
    public static void main(String[] args) throws Exception {
        BonusPool bonusPool = new BonusPool(10, 200, 0.9);
        int totalBonus = 0;
        while (true) {
            int bonus = bonusPool.popBonus();
            totalBonus += bonus;
            System.out.println("Current Bonus is:" + bonus);
        }
    }

    public BonusPool(Integer bonusBase, Integer bonusMax, double lossRate) {
        bonusBase_ = bonusBase;
        bonusMax_ = bonusMax;
        lossRate_ = lossRate;
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

    private boolean generateBonus() {
        bonusList_.clear();
        int totalBonus = (int)(bonusBase_ * POOLSIZE * lossRate_);
        int poolLossSize = (int)(POOLSIZE * POOLLOSSRATE);
        int profitSize = POOLSIZE - poolLossSize;

        // generate loss bonus
        int totalLost = 0;
        while (poolLossSize-- != 0) {
            int bonus = (int)(bonusRandom_.nextDouble() * bonusBase_);
            if (bonus == 0) {
                bonus++;
            }
            totalLost += bonus;
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
            if ((bonus + profitList.get(index)) > bonusMax_) {
                bonus = bonusMax_ - profitList.get(index);
                profitList.remove(index);
                bonusList_.add(bonusMax_);
            }
            else if (bonus + profitList.get(index) < bonusBase_) {
                bonus = bonusBase_ - profitList.get(index);
                profitList.remove(index);
                bonusList_.add(bonusBase_);
            }
            else {
                profitList.set(index, profitList.get(index) + bonus);
            }
            totalError -= bonus;
        }

        for (Integer bonus : profitList) {
            bonusList_.add(bonus);
        }

        int total = 0;
        for (Integer bonus : bonusList_) {
            total += bonus;
        }

        return true;
    }

    private Integer bonusBase_;
    private Integer bonusMax_;
    private double lossRate_;
    private final static Integer POOLSIZE = 10000;
    private final static Double POOLLOSSRATE = 0.95;
    private List<Integer> bonusList_ = new ArrayList<Integer>(POOLSIZE);
    private Random indexRandom_ = new Random();
    private Random bonusRandom_ = new Random();
}
