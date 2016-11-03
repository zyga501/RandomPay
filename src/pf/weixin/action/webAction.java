package pf.weixin.action;

import framework.action.AjaxActionSupport;
import pf.database.BonusPool;
import pf.database.MenuTree;
import pf.database.PayReturn;
import pf.database.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebAction extends AjaxActionSupport {

    private List<Object> menulist = new ArrayList<>();
    public List<Object> getMenulist() {
        return menulist;
    }

    public void setMenulist(List<Object> menulist) {
        this.menulist = menulist;
    }

    public String getPayReturn() {
        Map<String, Object> resuleMap = new HashMap<>();
        List<PayReturn> payReturnList = PayReturn.getPayReturn();
        if (payReturnList.size() > 0) {
            resuleMap.put("rlist",payReturnList);
//            resuleMap.put("rtScale", String.valueOf(payReturnList.get(0).getRtscale()));
//            resuleMap.put("commRate", String.valueOf(payReturnList.get(0).getCommrate()));
            return AjaxActionComplete(resuleMap);
        }
        else {
            resuleMap.put("rtScale", "0.0");
            resuleMap.put("commRate", "0.0");
        }
        return  AjaxActionComplete(resuleMap);
    }

    public String  signIn(){
        User userpara =new User();
        userpara.setUname(getParameter("loginname").toString());
        userpara.setUpwd(getParameter("password").toString());
        User user = User.getUser(userpara);
        if (null!= user){
            setAttribute("userid",user.getId());
            return AjaxActionComplete(true);
        }
        return AjaxActionComplete(false);
    }

    public String Mainpage() {
        if (getAttribute("userid").equals("")) return "";
       // menulist = MenuTree.getAllMenuNode();
        List<MenuTree> menutreelist = MenuTree.getMenuNode(0);
        for (MenuTree m : menutreelist) {
            List<MenuTree> prem = MenuTree.getMenuNode((int) m.getId());
            Map mapitem = new HashMap();
            mapitem.put("prenode",m);
            mapitem.put("subnode",prem);
            menulist.add(mapitem);
        }
        return "mainpagejsp";
    }

    public  String Logout(){
        getRequest().getSession().invalidate();
        return "loginjsp";
    }

    public String getBonusPoolReturn(){
        List<Map> bonusPool =BonusPool.groupBonus();
       return AjaxActionComplete(bonusPool);
    }

    public String updateRtScale() {
        try {
            PayReturn payReturn = new PayReturn();
            payReturn.setId(1);
            payReturn.setPaynum(Integer.parseInt(getParameter("rta").toString()));
            payReturn.setRtmin(Integer.parseInt(getParameter("rta1").toString()));
            payReturn.setRtmax(Integer.parseInt(getParameter("rta2").toString()));
            payReturn.setRtscale(Float.parseFloat(getParameter("rtScale").toString()));
            PayReturn.updateRtScale(payReturn);
            payReturn.setId(2);
            payReturn.setPaynum(Integer.parseInt(getParameter("rtb").toString()));
            payReturn.setRtmin(Integer.parseInt(getParameter("rtb1").toString()));
            payReturn.setRtmax(Integer.parseInt(getParameter("rtb2").toString()));
            payReturn.setRtscale(Float.parseFloat(getParameter("rtScale").toString()));
            PayReturn.updateRtScale(payReturn);
            payReturn.setId(3);
            payReturn.setPaynum(Integer.parseInt(getParameter("rtc").toString()));
            payReturn.setRtmin(Integer.parseInt(getParameter("rtc1").toString()));
            payReturn.setRtmax(Integer.parseInt(getParameter("rtc2").toString()));
            payReturn.setRtscale(Float.parseFloat(getParameter("rtScale").toString()));
            PayReturn.updateRtScale(payReturn);
            payReturn.setId(4);
            payReturn.setPaynum(Integer.parseInt(getParameter("rtd").toString()));
            payReturn.setRtmin(Integer.parseInt(getParameter("rtd1").toString()));
            payReturn.setRtmax(Integer.parseInt(getParameter("rtd2").toString()));
            payReturn.setRtscale(Float.parseFloat(getParameter("rtScale").toString()));
            PayReturn.updateRtScale(payReturn);
            return AjaxActionComplete(true);
        }
        catch (Exception e) {
            return AjaxActionComplete(false);
        }
    }

    public String updateCommRate() {
        if (PayReturn.updateCommRate(Double.parseDouble(getParameter("commRate").toString())))
            return AjaxActionComplete(true);
        return AjaxActionComplete(false);
    }

    public void Exportdetail() throws Exception {
        /*Map<Object, Object> param= new HashMap<>();
        //try
        {
            List<OrderInfo> lo = OrderInfo.getOrderInfo();
        }//catch (Exception e)
        {
            // e.printStackTrace();
            Map map=new HashMap<>();
            map.put("errorMessage","查无信息");
            // return AjaxActionComplete(map);
        }
        getResponse().setHeader("Content-Disposition", new String(
                ("attachment;filename=" + "Querydata"+String.valueOf((new Date()).getTime())+".xls").getBytes("GB2312"),
                "UTF-8"));
        WritableWorkbook wwb;
        OutputStream os = getResponse().getOutputStream();
        wwb = Workbook.createWorkbook(os);
        WritableSheet sheet = wwb.createSheet("查询结果", 0);
        Label label;
        int x = 0;
        for (HashMap m : lo) {
            int y = 0;
            label = new Label(0, x, String.valueOf(x + 1));
            sheet.addCell(label);
            Iterator iter = m.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                label = new Label(y += 1, x, null != val ? val.toString() : "");
                sheet.addCell(label);
            }
            x += 1;
        }
        wwb.write();
        wwb.close();
        os.flush();
        os.close();*/
    }
}