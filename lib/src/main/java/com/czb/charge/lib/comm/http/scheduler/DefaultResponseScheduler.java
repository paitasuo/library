package com.czb.charge.lib.comm.http.scheduler;

import com.czb.charge.lib.comm.http.ResponseScheduler;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class DefaultResponseScheduler implements ResponseScheduler {
    @Override
    public void schedule(Runnable runnable) {
        runnable.run();
    }
}
