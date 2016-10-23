package pf.weixin.api;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import framework.utils.HttpUtils;
import framework.utils.XMLParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import pf.ProjectLogger;
import pf.weixin.api.RequestBean.RequestData;
import pf.weixin.utils.Signature;

import java.util.Map;

public abstract class WeixinAPIWithSign extends WeixinAPI {
    public Map<String, Object> getResponseResult() {
        return responseResult_;
    }

    public boolean postRequest(String apiKey) throws Exception {
        if (!requestData_.checkParameter() || apiKey.isEmpty()) {
            ProjectLogger.error(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.buildSign(apiKey);

        System.out.println("apiKey:"+apiKey);
        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }

        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(requestData_);
        System.out.println("postDataXML:"+postDataXML);
        HttpPost httpPost = new HttpPost(apiUri);
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        String responseString = new String();
        try {
            responseString = HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse) -> {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            });
        }
        finally {
            httpPost.abort();
        }

        boolean ret = parseResponse(responseString, apiKey) && handlerResponse(responseResult_);

        if (!ret) {
            ProjectLogger.error("Request Url:\r\n" + apiUri);
            ProjectLogger.error("Response Data:\r\n" + responseString);
        }

        return ret;
    }

    protected boolean parseResponse(String ...args) throws Exception {
        responseResult_ = XMLParser.convertMapFromXml(args[0]);
        if (!Signature.checkSignValid(responseResult_, args[1])) {
            ProjectLogger.error(this.getClass().getName() + " CheckSignValid Failed!");
            return false;
        }
        return true;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }

    protected RequestData requestData_;
    private Map<String, Object> responseResult_;
}
