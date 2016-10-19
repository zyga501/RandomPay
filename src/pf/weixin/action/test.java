package pf.weixin.action;

import framework.action.AjaxActionSupport;
import pf.utils.BonusPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test extends AjaxActionSupport {

    public String aa() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("State", "恭喜您，抽到了" +  100 + "元红包！");
        int maxv =   1800;
        int[] aryint = BonusPool.getRandomArarray(Integer.parseInt("11"),90, maxv);
        List<Integer> hbList = new ArrayList<>();
        for (int i = 0; i < aryint.length; i++)
            hbList.add(i, aryint[i]);
        resultMap.put("hblist", hbList);
        return AjaxActionComplete(resultMap);
    }
}
