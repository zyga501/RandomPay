package pf.database;

public class User {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setUname("admin");
        user.setUpwd("123");
        user = User.getUser(user);
    }

    public static User getUser(User user) {
        String statement = "pf.database.mapping.user.getUser";
        return Database.Instance().selectOne(statement, user);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id_) {
        this.id_ = id_;
    }

    public String getUname() {
        return uname_;
    }

    public void setUname(String uname_) {
        this.uname_ = uname_;
    }

    public String getUpwd() {
        return upwd_;
    }

    public void setUpwd(String upwd_) {
        this.upwd_ = upwd_;
    }

    private long id_;
    private String uname_;
    private String upwd_;
}
