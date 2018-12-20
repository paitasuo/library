package com.czb.charge.lib.comm.http.scheduler;

import com.czb.charge.lib.comm.http.RequestScheduler;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class DefaultRequestScheduler implements RequestScheduler {

    @Override
    public void schedule(Runnable runnable) {
        runnable.run();
    }
}
