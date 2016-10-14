package pf.database.merchant;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {

    }

    public static MerchantInfo getMerchantInfoById(long id) {
        String statement = "pf.database.merchant.mapping.merchantInfo.getMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public static MerchantInfo getMerchantInfoByAppId(String appid) {
        String statement = "pf.database.merchant.mapping.merchantInfo.getMerchantInfoByAppId";
        return Database.Instance().selectOne(statement, appid);
    }

    public Long getId() {
        return id_;
    }

    public void setId(Long id) {
        this.id_ = id;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    private Long id_;
    private String name_;
}
