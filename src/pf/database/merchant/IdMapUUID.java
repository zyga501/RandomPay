package pf.database.merchant;

public class IdMapUUID {
    public static void main(String[] args) throws Exception {
    }

    public static IdMapUUID getMappingByUUID(String odod) {
        String statement = "pf.database.merchant.mapping.idMapUUID.getMappingByUUID";
        return Database.Instance().selectOne(statement, odod);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getUuid() {
        return uuid_;
    }

    public void setUuid(String uuid) {
        this.uuid_ = uuid;
    }

    private long id_;
    private String uuid_;
}
