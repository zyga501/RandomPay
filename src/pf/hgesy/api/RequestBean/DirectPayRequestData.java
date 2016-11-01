package pf.hgesy.api.RequestBean;

import framework.utils.IdWorker;
import framework.utils.StringUtils;
import pf.ProjectSettings;

public class DirectPayRequestData extends RequestData {
    public DirectPayRequestData() {
        order_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        request_time = StringUtils.generateDate("yyyyMMddhhmmss", "GMT+8");
        product_code = "C3";
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !account.isEmpty()
                    && !request_time.isEmpty()
                    && !order_no.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String account;
    public String order_no;
    public String product_code;
    public String request_time;
    public String subject;
    public double total_fee;
}
