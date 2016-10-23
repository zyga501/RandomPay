package pf.chanpay.api;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import pf.chanpay.utils.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class Gateway {

    private static String MERCHANT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB";
    private static String MERCHANT_PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAPl0dwpn+Z5i0945p2aejbeDUZ0bbxq/47Kwh7SWO0x02XLWhzjx77Zuc6GjCt9ZbHLv1+BOp7uKFrUF59Yfo/v2qZ26D3OSh1CgzkGgmaP8xfu/8jQGdSGri2NfrOx3LxuoUPk/J6nKBhKVLiWbxIrP+3HpHTItZebQAmRhzcIBAgMBAAECgYEA5MZzBhAlVaS9OekWJX7z6YF9r+AhHR8u7AoJ6wx8hboEUtV8XjVD6sOROIQWzI9ublqYWRbJra0O9iXcYPvTnskqArT/ykD6Z3ZBO0VNeMvz+lPuwdVBW4aR0H4fakSyuWpLzszlNSF+y/JCbjNSgFsvqYhSxCzbjVfxrYA7XgECQQD+ANdCWfwxJxfjLGe8flN1nI+dwO6KHXXIF4dPDHlANYnzvNFUy62QvShGbUQaAvA8VeVMB/icgjbB+sMxog85AkEA+2p4k/rYQBDCMYzJZ8Q6k/zK3ER2+oGC5b8zq5AXvev7R7kn6Ze1dQATS2MSjJ961FHPBkB641D0W9wPPWwBCQJBAOJ+T3R8FxShXnyVER+Uqck7RRkPe+WupWM6D87eOZSW6R9tpF/viyKAedRrBIoJSr0SfvfQN9Efj8JjYAD3GIkCQQDttIXai6GoOPASPyUxmcM0ph760TSYtpiXNgbZX7xC0fnbn0EQR2LGA1M6xKoGEMbmNY8CPzaw85qFu3MhRJtZAkEAupVO/a2co2K84W9yz6R4Vf7zDc2voN/IsL7Twv4U9jI0251EtxeoPK1123g1kldes52CdJDVfDjXTPW0Wv9I4A==";
    private static String charset = "UTF-8";

    public static String buildRequest(Map<String, String> sParaTemp, String signType, String key, String inputCharset, String gatewayUrl) throws Exception {
        // 待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp, signType, key, inputCharset);

        System.out.println(createLinkString(sPara, true));

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        // 设置编码集
        request.setCharset(inputCharset);

        request.setMethod(HttpRequest.METHOD_POST);

        request.setParameters(generatNameValuePair(createLinkRequestParas(sPara), inputCharset));
        request.setUrl(gatewayUrl);
        HttpResponse response = httpProtocolHandler.execute(request, null, null);
        if (response == null) {
            return null;
        }

        String strResult = response.getStringResult();

        return strResult;
    }

    private static NameValuePair[] generatNameValuePair(Map<String, String> properties, String charset) throws Exception {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            // nameValuePair[i++] = new NameValuePair(entry.getKey(), URLEncoder.encode(entry.getValue(),charset));
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }

    public static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, String signType, String key, String inputCharset) throws Exception {
        // 除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(sParaTemp);
        // 生成签名结果
        String mysign = "";
        if ("MD5".equalsIgnoreCase(signType)) {
            mysign = buildRequestByMD5(sPara, key, inputCharset);
        } else if ("RSA".equalsIgnoreCase(signType)) {
            mysign = buildRequestByRSA(sPara, key, inputCharset);
        }

        // 签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", signType);

        return sPara;
    }

    public static String buildRequestByMD5(Map<String, String> sPara, String key, String inputCharset) throws Exception {
        String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = MD5.sign(prestr, key, inputCharset);
        return mysign;
    }

    public static String buildRequestByRSA(Map<String, String> sPara, String privateKey, String inputCharset) throws Exception {
        String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = RSA.sign(prestr, privateKey, inputCharset);
        return mysign;
    }

    public static Map<String, String> createLinkRequestParas(Map<String, String> params) {
        Map<String, String> encodeParamsValueMap = new HashMap<String, String>();
        List<String> keys = new ArrayList<String>(params.keySet());
        String charset = params.get("_input_charset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value;
            try {
                value = URLEncoder.encode(params.get(key), charset);
                encodeParamsValueMap.put(key, value);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return encodeParamsValueMap;
    }

    public static String createLinkString(Map<String, String> params, boolean encode) {

        // params = paraFilter(params);

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        String charset = params.get("_input_charset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value = URLEncoder.encode(value, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            // try {
            // value = URLEncoder.encode(value,charset);
            // } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
            // }
            result.put(key, value);
        }

        return result;
    }

    public void gatewayPost(Map<String, String> origMap, String charset, String MERCHANT_PRIVATE_KEY) {
        try {
            String urlStr = "https://tpay.chanpay.com/mag/gateway/receiveOrder.do?";
            String resultString = this.buildRequest(origMap, "RSA", MERCHANT_PRIVATE_KEY, charset, urlStr);
            System.out.println(resultString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 单笔支付
     */
    public void createTrade() {

        Map<String, String> origMap = new HashMap<String, String>();
        origMap.put("version", "1.0");
        origMap.put("partner_id", "200000660194"); // 畅捷支付分配的商户号
        origMap.put("_input_charset", charset);
        origMap.put("is_anonymous", "Y");
        origMap.put("bank_code", "TESTBANK");// 含义看文档
        origMap.put("out_trade_no", (UUID.randomUUID().toString()).replace("-", ""));
        origMap.put("pay_method", "1");// 含义看文档
        origMap.put("pay_type", "C,DC");// 含义看文档
        origMap.put("service", "cjt_create_instant_trade");
        origMap.put("trade_amount", "0.01");
        origMap.put("notify_url", "http://tadmin.chanpay.com/tpu/mag/syncNotify.do");
        origMap.put("return_url", "http://www.baidu.com");
        this.gatewayPost(origMap, charset, MERCHANT_PRIVATE_KEY);
    }

    private String encrypt(String src, String publicKey, String charset) {
        try {
            byte[] bytes = RSA.encryptByPublicKey(src.getBytes(charset), publicKey);
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Gateway test = new Gateway();
        test.createTrade();
    }
}
