package com.czb.charge.lib.comm;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Author: paitasuo
 * Date: 2018/11/5
 * Description:App管理器，用于管理所有的activity 使用时注意堆栈后进先出原则
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后压入的），可能为null
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && activityStack.size() > 0) {
            try {
                activity = activityStack.lastElement();
            } catch (NoSuchElementException e) {
                activity = null;
            }
        }
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后压入的）
     */
    public void finishActivity() {
        Activity activity = null;
        if (activityStack != null && activityStack.size() > 0) {
            try {
                activity = activityStack.lastElement();
            } catch (NoSuchElementException e) {
                activity = null;
            }
        }
        finishActivity(activity);
    }

    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(String className) {
        try {
            Activity removeActivity = null;
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(className)) {
                    removeActivity = activity;
                    break;
                }
            }
            if (removeActivity != null) {
                finishActivity(removeActivity);
            }

        } catch (Exception e) {

        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivityFullPath(String className) {
        try {
            Class clz = Class.forName(className);
            Activity removeActivity = null;
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(clz)) {
                    removeActivity = activity;
                    break;
                }
            }
            if (removeActivity != null) {
                finishActivity(removeActivity);
            }

        } catch (Exception e) {

        }
    }

    public void finishStackActivity(String className) {
        try {
            Class clz = Class.forName(className);
            Activity removeActivity = null;
            int targetIndex = -1;
            for (int i = 0; i < activityStack.size(); i++) {
                Activity activity = activityStack.get(i);
                if (activity.getClass().equals(clz)) {
                    targetIndex = i;
                    break;
                }
            }
            for (int i = activityStack.size() - 1; i >= targetIndex + 1; i--) {
                if (i < activityStack.size()) {
                    Activity target = activityStack.get(i);
                    if (target != null)
                        finishActivity(target);
                }
            }

        } catch (Exception e) {

        }
    }

    /**
     * 得到指定的Activity
     *
     * @param cls
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    public List<Activity> getActivitys(Class<?> cls) {
        List<Activity> acties = new ArrayList<>();
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                acties.add(activity);
            }
        }
        return acties;
    }


    public void finishAllActivity(Class<?> cls) {
        loopRemoveStack(loopRemoveAct(cls, -1));
    }

    private List<Integer> loopRemoveAct(Class<?> cls, int exceptIndex) {
        List<Integer> delIndexs = new ArrayList<>();
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity act = activityStack.get(i);
            if (null != act && act.getClass().equals(cls)) {
                if (i == exceptIndex) break;
                delIndexs.add(i);
                activityStack.get(i).finish();
            }
        }
        return delIndexs;
    }

    private void loopRemoveStack(List<Integer> mIndexs) {
        int delCount = 0;
        for (int i = mIndexs.size() - 1; i >= 0; i--) {
            int delIndex = mIndexs.get(i);
            delIndex -= delCount;
            if (delIndex > activityStack.size() - 1) return;
            activityStack.remove(delIndex);
            delIndex++;
        }
    }

    public void finishAllActivityExceptFirst(Class<?> cls) {
        loopRemoveStack(loopRemoveAct(cls, scanFirstClsIndex(cls)));
    }

    private int scanFirstClsIndex(Class<?> cls) {
        int result = 0;
        for (int i = activityStack.size() - 1; i >= 0; i--) {

            Activity act = activityStack.get(i);
            if (null != act && act.getClass().equals(cls)) {
                result = i;
            }
        }
        return result;
    }

    /**
     * 结束所有的
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 除传入的类以外， 其余类finish
     */
    public void finishActivityExcept(Class clazz) {
        Activity act = null;

        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && !activityStack.get(i).getClass().equals(clazz)) {
                activityStack.get(i).finish();
            } else {
                act = activityStack.get(i);
            }
        }
        activityStack.clear();
        activityStack.add(act);
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回app运行状态
     * 1:程序在前台运行
     * 2:程序在后台运行
     * 3:程序未启动
     * 注意：需要配置权限<uses-permission android:name="android.permission.GET_TASKS" />
     */
    public int getAppSatus(Context context, String pageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);
        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return 1;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return 2;
                }
            }
            return 3;//栈里找不到，返回3
        }
    }
}
