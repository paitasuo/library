package com.czb.charge.lib.comm.http.scheduler;

import com.czb.charge.lib.comm.http.ResponseScheduler;

/**
 * Author: hfyd
 * Date: 2018/12/20
 * Description:
 */
public class SchedulerFactory {

    private static ResponseScheduler mainRes = new AndroidMainThreadResponseScheduler();
    private static ResponseScheduler ioRes = new DefaultResponseScheduler();

    public static ResponseScheduler main(){
        return mainRes;
    }

    public static ResponseScheduler io(){
        return ioRes;
    }
}
