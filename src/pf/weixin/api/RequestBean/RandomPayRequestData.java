package pf.weixin.api.RequestBean;

import framework.utils.IdWorker;
import framework.utils.StringUtils;
import pf.ProjectSettings;
import pf.weixin.utils.Signature;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RandomPayRequestData {
    public RandomPayRequestData() throws UnknownHostException {
        spbill_create_ip = InetAddress.getLocalHost().getHostAddress().toString();
        partner_trade_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        nonce_str = StringUtils.generateRandomString(32);
    }

    public boolean checkParameter() {
        try {
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public void buildSign(String apiKey) throws IllegalAccessException {
        this.sign = Signature.generateSign(this, apiKey);
    }

    public String mch_appid;
    public String mchid;
    public String nonce_str;
    public String spbill_create_ip;
    public String partner_trade_no;
    public String sign;
    public String openid;
    public String check_name;
    public Integer amount;
    public String desc;
}
