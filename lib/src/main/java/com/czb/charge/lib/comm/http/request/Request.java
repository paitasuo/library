package com.czb.charge.lib.comm.http.request;

import com.czb.charge.lib.comm.http.HttpCall;
import com.czb.charge.lib.comm.http.OnHttpCallBack;
import com.czb.charge.lib.comm.http.RequestScheduler;
import com.czb.charge.lib.comm.http.ResponseHandler;
import com.czb.charge.lib.comm.http.ResponseScheduler;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
abstract class Request {

    HttpCall mHttpCall;
    ResponseHandler mResponseHandler;
    RequestScheduler mRequestScheduler;
    ResponseScheduler mResponseScheduler;

    public Request(HttpCall mHttpCall, ResponseHandler mResponseHandler
            , RequestScheduler mRequestScheduler, ResponseScheduler mResponseScheduler) {
        this.mHttpCall = mHttpCall;
        this.mResponseHandler = mResponseHandler;
        this.mRequestScheduler = mRequestScheduler;
        this.mResponseScheduler = mResponseScheduler;
    }

    public abstract Request execute(OnHttpCallBack callBack);

    public abstract void cancel();
}
