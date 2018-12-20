package com.czb.charge.lib.comm.http.scheduler;

import android.os.Handler;
import android.os.Looper;
import com.czb.charge.lib.comm.http.ResponseScheduler;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class AndroidMainThreadResponseScheduler implements ResponseScheduler {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void schedule(Runnable runnable) {
        mainHandler.post(runnable);
    }
}
