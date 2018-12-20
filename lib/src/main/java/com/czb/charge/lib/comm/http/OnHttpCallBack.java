package com.czb.charge.lib.comm.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public abstract class OnHttpCallBack<T> implements HttpCallBack {

    private ResponseHandler responseHandler;
    private Class<T> clazz;

    protected ResponseScheduler mResponseScheduler;

    public ResponseScheduler getResponseScheduler() {
        return mResponseScheduler;
    }

    public void setResponseScheduler(ResponseScheduler responseScheduler) {
        this.mResponseScheduler = responseScheduler;
    }

    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;

        Type type = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        clazz = (Class<T>) trueType;
    }

    @Override
    public final void onSuccess(String result) {
        if (responseHandler == null)
            throw new RuntimeException("ResponseHandler must not null");
        T t = null;
        try {
            t = responseHandler.handle(result, clazz);
        }catch (Exception e) {
            onError(e);
        }

        final T finaT = t;
        mResponseScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                onSuccess(finaT);
            }
        });

    }

    public final void onError(final Throwable e){
        mResponseScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                onError(e);
            }
        });
    }

    public abstract void onError(Exception e);

    public abstract void onSuccess(T t);
}
