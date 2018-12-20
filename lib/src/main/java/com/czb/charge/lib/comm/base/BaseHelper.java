package com.czb.charge.lib.comm.base;

import com.czb.charge.lib.webview.CustomListener;

/**
 * Author: paitasuo
 * Date: 2018/12/19
 * Description:
 */
public abstract class BaseHelper {

    private static CustomListener mCustomListener;

    public static CustomListener getListener() {
        return mCustomListener;
    }

    public static void setJsObject(CustomListener customListener) {
        mCustomListener  = customListener;
    }

}
