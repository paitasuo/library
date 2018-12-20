package com.czb.charge.lib.comm.http;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public interface HttpCallBack {


    void onSuccess(String result);


    void onError(Throwable e);
}
