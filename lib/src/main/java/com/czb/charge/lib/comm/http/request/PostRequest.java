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
public class PostRequest extends Request {

    private String url;
    private Map<String, String> params;


    public PostRequest(HttpCall httpCall, ResponseHandler responseHandler,
                       RequestScheduler requestScheduler, ResponseScheduler responseScheduler) {
        super(httpCall, responseHandler, requestScheduler, responseScheduler);
    }

    public PostRequest requestOn(RequestScheduler requestScheduler) {
        this.mRequestScheduler = requestScheduler;
        return this;
    }

    public PostRequest responseOn(ResponseScheduler responseScheduler){
        this.mResponseScheduler = responseScheduler;
        return this;
    }

    @Override
    public Request execute(OnHttpCallBack callBack) {
        callBack.setResponseHandler(mResponseHandler);
        callBack.setResponseScheduler(mResponseScheduler);
        mHttpCall.setHeaders(headers);
        mHttpCall.post(url, params, callBack);
        return this;
    }

    @Override
    public void cancel() {
        mHttpCall.cancel();
    }

    private Map<String, String> headers = new HashMap<>();

    public PostRequest addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public PostRequest url(String url) {
        this.url = url;
        return this;
    }

    public PostRequest params(Map<String, String> params) {
        this.params = params;
        return this;
    }
}
