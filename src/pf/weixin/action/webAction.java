package pf.weixin.action;

import framework.action.AjaxActionSupport;
import pf.database.MenuTree;
import pf.database.User;

import java.util.*;

public class webAction extends AjaxActionSupport {

    private List<Object> menulist = new ArrayList<>();

    public List<Object> getMenulist() {
        return menulist;
    }

    public void setMenulist(List<Object> menulist) {
        this.menulist = menulist;
    }

    public String  signIn(){
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

}