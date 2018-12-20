package com.czb.charge.lib.comm.http;

import com.czb.charge.lib.comm.http.request.GetRequest;
import com.czb.charge.lib.comm.http.request.PostRequest;
import com.czb.charge.lib.comm.http.scheduler.AndroidMainThreadResponseScheduler;
import com.czb.charge.lib.comm.http.scheduler.DefaultRequestScheduler;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class HttpClient {

    private HttpCall mHttpCall;
    private ResponseHandler mResponseHandler;
    private RequestScheduler mRequestScheduler;
    private ResponseScheduler mResponseScheduler;

    public HttpClient(HttpCall httpCall, ResponseHandler responseHandler
            , RequestScheduler requestScheduler, ResponseScheduler responseScheduler) {
        this.mHttpCall = httpCall;
        this.mResponseHandler = responseHandler;
        this.mRequestScheduler = requestScheduler;
        this.mResponseScheduler = responseScheduler;
    }

    public GetRequest get() {
        return new GetRequest(mHttpCall, mResponseHandler, mRequestScheduler, mResponseScheduler);
    }

    public PostRequest post() {
        return new PostRequest(mHttpCall, mResponseHandler, mRequestScheduler, mResponseScheduler);
    }

    public static class Builder {

        private HttpCall httpCall;
        private ResponseHandler responseHandler;
        private RequestScheduler requestScheduler;
        private ResponseScheduler responseScheduler;

        public Builder setHttpCall(HttpCall httpCall) {
            this.httpCall = httpCall;
            return this;
        }

        public Builder setResponseHandler(ResponseHandler responseHandler) {
            this.responseHandler = responseHandler;
            return this;
        }

        public Builder setRequestScheduler(RequestScheduler requestScheduler) {
            this.requestScheduler = requestScheduler;
            return this;
        }

        public Builder setResponseScheduler(ResponseScheduler responseScheduler) {
            this.responseScheduler = responseScheduler;
            return this;
        }

        public HttpClient build() {
            if (httpCall == null) {
                throw new RuntimeException("httpCall must not null");
            }

            if (responseHandler == null) {
                throw new RuntimeException("responseHandler must not null");
            }

            if (requestScheduler == null) {
                requestScheduler = new DefaultRequestScheduler();
            }

            if (responseScheduler == null) {
                responseScheduler = new AndroidMainThreadResponseScheduler();
            }
            return new HttpClient(httpCall, responseHandler, requestScheduler, responseScheduler);
        }
    }
}
