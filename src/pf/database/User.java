package pf.database;

public class User {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setUname("admin");
        user.setUpwd("123");
        user = User.getUser(user);
        user.getId();
    }

    public static User getUser(User user) {
        String statement = "pf.database.mapping.user.getUser";
        return Database.Instance().selectOne(statement, user);
    }

    public long getId() {
        return id;
    }

    public void setId(long id_) {
        this.id = id_;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname_) {
        this.uname = uname_;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd_) {
        this.upwd = upwd_;
    }

    private long id;
    private String uname;
    private String upwd;
}
