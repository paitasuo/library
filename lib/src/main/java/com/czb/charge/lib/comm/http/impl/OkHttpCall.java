package com.czb.charge.lib.comm.http.impl;

import android.util.Log;

import com.czb.charge.lib.comm.http.HttpCall;
import com.czb.charge.lib.comm.http.HttpCallBack;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class OkHttpCall extends HttpCall {

    private Call call;

    @Override
    public void post(String url, Map<String, String> params, final HttpCallBack callBack) {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();

        if (params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                bodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = bodyBuilder.build();

        Request request = new Request.Builder().url(url).post(body).build();

        call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, final Response response) {
                if (response.isSuccessful() && callBack != null) {
                    try {
                        callBack.onSuccess(response.body().string());
                    } catch (IOException e) {
                        callBack.onError(e);
                    }
                } else {
                    //callBack.onError(e);
                }

            }
        });
    }

    @Override
    public void get(String url, final HttpCallBack callBack) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, final Response response) {
                if (response.isSuccessful() && callBack != null) {
                    try {
                        callBack.onSuccess(response.body().string());
                    } catch (IOException e) {
                        callBack.onError(e);
                    }
                } else {
                    //callBack.onError(e);
                }
            }
        });
    }

    @Override
    public void cancel() {
        if (call != null && !call.isCanceled())
            call.cancel();
    }
}
