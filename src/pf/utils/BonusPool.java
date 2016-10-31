package pf.utils;

import pf.database.PayReturn;

import java.util.*;

public class BonusPool {
    public static void main(String[] args) throws Exception {
        int totalBonus = 0;
        do {
            int bonus = BonusPool.getBonus(2000);
            totalBonus += bonus;
            pf.database.BonusPool.deleteBonus(new pf.database.BonusPool(2000, bonus));
        } while (BonusPool.getBonusSize(2000) != 0);

        System.out.println("BonusBase:" + 10 + " BonusMax:" + 200 + " LossRate:" + 0.9 + " PoolSize:" + 200);
        System.out.println("Total Bonus:" + totalBonus);
        System.out.println();
    }

    public static int getBonus(int amount) {
        synchronized (bonusPoolMap) {
            int amountArray[] = {1000, 2000, 5000, 10000};
            int minIndex = 0;
            int minValue = 100000;
            for (int index = 0; index < amountArray.length; index++) {
                if (Math.abs(amount - amountArray[index]) < minValue) {
                    minIndex = index;
                    minValue = Math.abs(amount - amountArray[index]);
                }
            }

            return bonusPoolMap.get(amountArray[minIndex]).popBonus();
        }
    }

    public static void deleteBonus(int amount, int bonus) {
        int amountArray[] = {1000, 2000, 5000, 10000};
        int minIndex = 0;
        int minValue = 100000;
        for (int index = 0; index < amountArray.length; index++) {
            if (Math.abs(amount - amountArray[index]) < minValue) {
                minIndex = index;
                minValue = Math.abs(amount - amountArray[index]);
            }
        }

        pf.database.BonusPool.deleteBonus(new pf.database.BonusPool(amountArray[minIndex], bonus));
    }

    public static int getBonusSize(int amount) {
        synchronized (bonusPoolMap) {
            return bonusPoolMap.get(amount).PoolSize();
        }
    }

    private static HashMap<Integer, BonusPool> bonusPoolMap = new HashMap<>();
    static {
        bonusPoolMap.put(1000, new BonusPool(1000));
        bonusPoolMap.put(2000, new BonusPool(2000));
        bonusPoolMap.put(5000, new BonusPool(5000));
        bonusPoolMap.put(10000, new BonusPool(10000));
    }

    public BonusPool(Integer bonusBase) {
        bonusBase_ = bonusBase;
        poolSize_ = 200;
        poolLossRate_ = 0.95;
        bonusList_ = new ArrayList<>(poolSize_);
    }

    public BonusPool(Integer bonusBase, Integer bonusMin, Integer bonusMax, Integer poolSize, Double poolLossRate) {
        bonusBase_ = bonusBase;
        poolSize_ = poolSize;
        poolLossRate_ = poolLossRate;
        bonusList_ = new ArrayList<>(poolSize);
    }

    public Integer popBonus() {
        synchronized (bonusList_) {
            if (bonusList_.size() == 0) {
                List<pf.database.BonusPool> bonusPoolList = pf.database.BonusPool.getBonusByAmount(bonusBase_);
                if (bonusPoolList.size() == 0) {
                    List<PayReturn> payReturnList = PayReturn.getPayReturn();
                    for (PayReturn payReturn : payReturnList) {
                        if (payReturn.getPaynum() * 100 == bonusBase_) {
                            while (!generateBonus(payReturn.getRtmin() * 100, payReturn.getRtmax() * 100, payReturn.getRtscale()));
                            for (int bonus : bonusList_) {
                                bonusPoolList.add(new pf.database.BonusPool(bonusBase_, bonus));
                            }
                            pf.database.BonusPool.insertBonus(bonusPoolList);
                            break;
                        }
                    }
                }
                else {
                    for (pf.database.BonusPool bonusPool : bonusPoolList) {
                        bonusList_.add(bonusPool.getBonus());
                    }
                }
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

    private boolean generateBonus(Integer bonusMin,Integer bonusMax, double lossRate) {
        bonusList_.clear();
        int totalBonus = (int)(bonusBase_ * poolSize_ * lossRate);
        int poolLossSize = (int)(poolSize_ * poolLossRate_);
        int profitSize = poolSize_ - poolLossSize;

        // generate loss bonus
        while (poolLossSize-- != 0) {
            int bonus = (int)(bonusRandom_.nextDouble() * bonusBase_) + bonusMin;
            if (bonus == 0) {
                bonus++;
            }
            totalBonus -= bonus;
            bonusList_.add(bonus);
        }

        if (profitSize * bonusMax < totalBonus)
            throw new IllegalArgumentException("根据当前参数无法生成有效的奖金池!");

        // generate profit bonus
        int totalProfit = 0;
        List<Integer> profitList = new ArrayList<>();
        while (profitSize-- != 0) {
            int bonus = bonusRandom_.nextInt(bonusMax - bonusBase_) + bonusBase_;
            totalProfit += bonus;
            profitList.add(bonus);
        }

        // fixed profit bonus
        int totalError = totalProfit - totalBonus;
        int errorBase = Math.max(totalError / (profitList.size() * 10), 1);
        while (totalError > 0) {
            if (profitList.size() <= 0) {
                return false;
            }

            int bonus = Math.min(bonusRandom_.nextInt(errorBase) + errorBase, totalError);
            int index = indexRandom_.nextInt(profitList.size());
            if ((profitList.get(index) - bonus) > bonusMax) {
                bonus = bonusMax - profitList.get(index);
                profitList.remove(index);
                bonusList_.add(bonusMax);
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
    private Integer poolSize_ = 1000;
    private Double poolLossRate_ = 0.95;
    private List<Integer> bonusList_;
    private Random indexRandom_ = new Random();
    private Random bonusRandom_ = new Random();
}
