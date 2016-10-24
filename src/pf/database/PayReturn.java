package pf.database;

import java.util.List;

public class PayReturn {
    public static void main(String[] args) throws Exception {
        List<PayReturn> lp = PayReturn.getPayReturn();
        for (PayReturn pr:lp ) {
            System.out.print(pr.paynum_);
            System.out.println(" ; "+pr.getRtscale());
        }
    }

    public static List<PayReturn> getPayReturn() {
        String statement = "pf.database.mapping.payreturn.getPayReturn";
        return Database.Instance().selectList(statement);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id_) {
        this.id_ = id_;
    }

    public int getPaynum() {
        return paynum_;
    }

    public void setPaynum(int paynum_) {
        this.paynum_ = paynum_;
    }

    public int getRtmin() {
        return rtmin_;
    }

    public void setRtmin(int rtmin_) {
        this.rtmin_ = rtmin_;
    }

    public int getRtmax() {
        return rtmax_;
    }

    public void setRtmax(int rtmax_) {
        this.rtmax_ = rtmax_;
    }

    public float getRtscale() {
        return rtscale_;
    }

    public void setRtscale(float rtscale_) {
        this.rtscale_ = rtscale_;
    }

    private long id_;
    private int paynum_;
    private int rtmin_;
    private int rtmax_;
    private float rtscale_;
}
