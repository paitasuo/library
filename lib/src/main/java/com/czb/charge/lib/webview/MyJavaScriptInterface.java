package com.czb.charge.lib.webview;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Author: paitasuo
 * Date: 2018/12/19
 * Description:
 */
public class MyJavaScriptInterface extends BaseJavaScriptInterface{

    private CustomListener mListener;
    private Activity  mActivity;


    public MyJavaScriptInterface(Activity activity  , CustomListener listener) {
        mListener = listener;
        mActivity = activity;
    }

    /**
     * @return 返回数据给前端
     * @JavascriptInterface 这个注解必须添加，否则js调不到这个方法
     * 这个方法名称也必须要和前端保持一致
     */
    @JavascriptInterface
    public void startNavigate(String startLat, String startLng, String endLat, String endLng) {
        //去做想做的事情。比如导航，直接带着开始和结束的经纬度Intent到导航activity就可以

        if (TextUtils.isEmpty(startLat) || TextUtils.isEmpty(startLng) || TextUtils.isEmpty(endLat)
                || TextUtils.isEmpty(endLng)) {//如果接收的数据不正确，给予提示
            Toast.makeText(mActivity, "有不正确的数据", Toast.LENGTH_LONG).show();
            return;
        }
        mListener.listener(mActivity,startLat, startLng, endLat, endLng);

    }

}
