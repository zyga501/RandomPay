package pf.utils;

import java.util.*;

public class BonusPool {
    public static void main(String[] args) throws Exception {
        ArrayList<Integer> mapping = new ArrayList<>();
        for (int index = 0; index < 200; ++index) {
            mapping.add(0);
        }

        int totalBonus = 0;
        BonusPool bonusPool = new BonusPool(1000, 100, 20000, 0.9);
        do {
            int bonus = bonusPool.popBonus();
            totalBonus += bonus;
            mapping.set(bonus - 1, mapping.get(bonus - 1) + 1);
        } while (bonusPool.PoolSize() != 0);

        System.out.println("BonusBase:" + 10 + " BonusMax:" + 200 + " LossRate:" + 0.9 + " PoolSize:" + 200);
        System.out.println("Total Bonus:" + totalBonus);
        System.out.println("Bonus Distributed:");
        for (int index = 0; index < mapping.size(); ++index) {
            totalBonus += mapping.get(index);
            System.out.println("Bonus " + (index + 1) + " : " + mapping.get(index));
        }
        System.out.println();
    }

    public static int getBonus(int amount) {
        synchronized (bonusPoolMap) {
            return bonusPoolMap.get(amount).popBonus();
        }
    }

    private static HashMap<Integer, BonusPool> bonusPoolMap = new HashMap<>();
    static {
        bonusPoolMap.put(1000, new BonusPool(1000, 100, 20000, 0.9));
        bonusPoolMap.put(2000, new BonusPool(2000, 100, 60000, 0.9));
        bonusPoolMap.put(5000, new BonusPool(5000, 100, 80000, 0.9));
        bonusPoolMap.put(10000, new BonusPool(10000, 100, 180000, 0.9));
    }

    public BonusPool(Integer bonusBase, Integer bonusMin,Integer bonusMax, double lossRate) {
        bonusBase_ = bonusBase;
        bonusMin_ = bonusMin;
        bonusMax_ = bonusMax;
        lossRate_ = lossRate;
        poolSize_ = 200;
        poolLossRate_ = 0.95;
        bonusList_ = new ArrayList<>(poolSize_);
    }

    public BonusPool(Integer bonusBase, Integer bonusMin, Integer bonusMax, double lossRate, Integer poolSize, Double poolLossRate) {
        bonusBase_ = bonusBase;
        bonusMin_ = bonusMin;
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
            int bonus = (int)(bonusRandom_.nextDouble() * bonusBase_) + bonusMin_;
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
        int errorBase = Math.max(totalError / profitList.size(), 1);
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

    public static float[] generateVirtualBonus(int bonusIndex, float bonus,int maxval){
        Random indexRand = new Random();
        int rd =indexRand.nextInt(3)+1;
        float[] intArray =new float[15];
        for (int i=0;i<rd;i++) {
            intArray[i] = (float) ((indexRand.nextInt((int) (bonus*100))+100)/100.00);
        }
        for (int i=rd;i<14;i++) {
            intArray[i] = (float) ((indexRand.nextInt(maxval*100)+100)/100.00);
        }
        float tmpint;
        for (int i=0;i<14;i++){
            int index = indexRand.nextInt(14);
            tmpint  =  intArray[i];
            intArray[i] = intArray[index];
            intArray[index]=tmpint;
        }
        intArray[14] = intArray[bonusIndex] ;
        intArray[bonusIndex] = bonus;
        return intArray;
    }

    private Integer bonusBase_;
    private Integer bonusMin_;
    private Integer bonusMax_;
    private double lossRate_;
    private Integer poolSize_ = 1000;
    private Double poolLossRate_ = 0.95;
    private List<Integer> bonusList_;
    private Random indexRandom_ = new Random();
    private Random bonusRandom_ = new Random();
}
