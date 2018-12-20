package com.czb.charge.lib.webview;

import android.app.Activity;

/**
 * Author: paitasuo
 * Date: 2018/12/19
 * Description:
 */
public interface CustomListener {
    void listener(Activity activity, String startLat, String startLng, String endLat, String endLng);
}
