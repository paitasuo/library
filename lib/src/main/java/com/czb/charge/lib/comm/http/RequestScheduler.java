package com.czb.charge.lib.comm.http;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public interface RequestScheduler {

    void schedule(Runnable runnable);
}
