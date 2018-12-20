package com.czb.charge.lib.comm.utils;

import android.util.Log;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class CzbLog {

    private static boolean eanble;
    private static String TAG = "CzbChargeSdk";

    public static void setEanble(boolean eanble) {
        CzbLog.eanble = eanble;
    }

    public static void error(String msg) {
        if (eanble)
            Log.e(TAG, msg);
    }

    public static void e(Throwable e) {
        if (eanble)
            error(Log.getStackTraceString(e));
    }

    public static void w(String msg) {
        if (eanble)
            Log.w(TAG, msg);
    }

    public static void i(String msg) {
        if (eanble)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (eanble)
            Log.d(TAG, msg);
    }
}
