package com.czb.charge.lib.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.czb.charge.lib.view.X5WebView;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: paitasuo
 * Date: 2018/12/19
 * Description:  webview 自带导航
 */
public class MyWebViewClientDefault extends WebViewClient {
    private Activity mActivity;

    private X5WebView mWebView;
    private String mKey;
    private String mValue;

    public MyWebViewClientDefault(Activity activity, X5WebView webView, String key, String value) {
        mActivity = activity;
        mWebView = webView;
        mKey = key;
        mValue = value;

    }

    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Log.e("url=", url);
        if (url.startsWith("weixin://") || url.contains("alipays://platformapi"))
        {//如果微信或者支付宝，跳转到相应的app界面,
            mWebView.goBack();
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(mActivity, "未安装相应的客户端", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        if (url.startsWith("androidamap://route")) {
            return true;
        }

        if (url.startsWith("http://m.amap.com")) {// http://m.amap.com
            webView.loadUrl(url);
            return true;
        }

        /**
         *
         * 设置 Header 头方法
         * window.czb.extraHeaders(String key, String value)
         */
        if (mKey != null) {
            Map extraHeaders = new HashMap();
            extraHeaders.put(mKey, mValue);
            webView.loadUrl(url, extraHeaders);
        } else {
            webView.loadUrl(url);
            Toast.makeText(mActivity, url, Toast.LENGTH_LONG).show();
        }
        return true;

    }
}
