package com.czb.charge.lib.comm.http.impl;

import com.czb.charge.lib.comm.http.HttpClient;
import com.czb.charge.lib.comm.http.scheduler.AndroidMainThreadResponseScheduler;
import com.czb.charge.lib.comm.http.scheduler.DefaultRequestScheduler;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class DefaultHttpClient {

    private static HttpClient mHttpClient;

    public static HttpClient getClient() {
        if (mHttpClient == null)
            mHttpClient = new HttpClient.Builder()
                    .setHttpCall(new OkHttpCall())
                    .setResponseHandler(new GsonResponseHandler())
                    .setResponseScheduler(new AndroidMainThreadResponseScheduler())
                    .setRequestScheduler(new DefaultRequestScheduler())
                    .build();
        return mHttpClient;
    }
}
