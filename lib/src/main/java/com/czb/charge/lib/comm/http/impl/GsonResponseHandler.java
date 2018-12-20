package com.czb.charge.lib.comm.http.impl;

import com.czb.charge.lib.comm.http.ResponseHandler;
import com.google.gson.Gson;


/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class GsonResponseHandler implements ResponseHandler {

    private static Gson gson = new Gson();

    @Override
    public <T> T handle(String result,Class<T> handlerClazz) {
        return gson.fromJson(result, handlerClazz);
    }
}
