package com.czb.charge.lib.comm.http.request;

import com.czb.charge.lib.comm.http.HttpCall;
import com.czb.charge.lib.comm.http.OnHttpCallBack;
import com.czb.charge.lib.comm.http.RequestScheduler;
import com.czb.charge.lib.comm.http.ResponseHandler;
import com.czb.charge.lib.comm.http.ResponseScheduler;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class GetRequest extends Request {

    public GetRequest(HttpCall httpCall, ResponseHandler responseHandler,
                      RequestScheduler requestScheduler, ResponseScheduler responseScheduler) {
        super(httpCall, responseHandler, requestScheduler, responseScheduler);
    }

    @Override
    public Request execute(OnHttpCallBack callBack) {
        callBack.setResponseHandler(mResponseHandler);
        callBack.setResponseScheduler(mResponseScheduler);
        String realUrl = getRealGetUrl();
        mHttpCall.setHeaders(headers);
        mHttpCall.get(realUrl, callBack);
        return this;
    }


    public GetRequest requestOn(RequestScheduler requestScheduler) {
        this.mRequestScheduler = requestScheduler;
        return this;
    }

    public GetRequest responseOn(ResponseScheduler responseScheduler) {
        this.mResponseScheduler = responseScheduler;
        return this;
    }

    @Override
    public void cancel() {
        mHttpCall.cancel();
    }

    private String getRealGetUrl() {
        if (params.size() > 0) {
            StringBuilder builder = new StringBuilder();
            String apandUrl = url;
            if (url.endsWith("/")) {
                apandUrl = url.substring(0, url.lastIndexOf("/") - 1);
            }
            builder.append(apandUrl).append("?");
            for (String key : params.keySet()) {
                String value = params.get(key);
                builder.append(key)
                        .append("=")
                        .append(value)
                        .append("&");
            }

            apandUrl = builder.toString();
            if (url.endsWith("&")) {
                apandUrl = builder.toString().substring(0, url.lastIndexOf("&") - 1);
            }


            return apandUrl;
        } else
            return url;
    }

    private String url;
    private Map<String, String> params;
    private Map<String, String> headers = new HashMap<>();

    public GetRequest addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public GetRequest params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public GetRequest url(String url) {
        this.url = url;
        return this;
    }
}
