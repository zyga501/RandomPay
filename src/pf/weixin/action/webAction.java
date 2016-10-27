package pf.weixin.action;

import framework.action.AjaxActionSupport;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import pf.ProjectSettings;
import pf.database.MenuTree;
import pf.database.OrderInfo;
import pf.database.User;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

public class webAction extends AjaxActionSupport {

    private List<Object> menulist = new ArrayList<>();

    public List<Object> getMenulist() {
        return menulist;
    }

    public void setMenulist(List<Object> menulist) {
        this.menulist = menulist;
    }

    public String  signIn(){
        System.out.println(ProjectSettings.getData("commrate").toString());
        ProjectSettings.setData("commrate","0.08");
        System.out.println(ProjectSettings.getData("commrate").toString());
        try{
            User userpara =new User();
            userpara.setUname(getParameter("loginname").toString());
            userpara.setUpwd(getParameter("password").toString());
            User user = User.getUser(userpara);
            if (null!= user){
                setAttribute("userid",user.getId());
                return AjaxActionComplete(true);
            }
        }
        catch (Exception e){
            return AjaxActionComplete(false);
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