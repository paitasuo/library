package com.czb.charge.lib.comm.http;

import java.util.Map;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public abstract class HttpCall {

    private Map<String,String> headers;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public abstract void post(String url, Map<String, String> params, HttpCallBack callBack);

    public abstract void get(String url, HttpCallBack callBack);

    public abstract void cancel();
}
