package com.czb.charge.lib.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.czb.charge.lib.view.X5WebView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: paitasuo
 * Date: 2018/12/19
 * Description:自定义导航
 */
public class MyWebViewClient extends WebViewClient {

    private Activity mActivity;

    private X5WebView mWebView;
    private String mKey;
    private String mValue;

    public MyWebViewClient(Activity activity, X5WebView webView, String key, String value) {
        mActivity = activity;
        mWebView = webView;
        mKey = key;
        mValue = value;

    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError
            sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
    }

    @Override
    public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
        super.onPageStarted(webView, s, bitmap);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Log.e("url=", url);
        if (!url.startsWith("weixin://") && !url.contains("alipays://platformapi")) {
            if (mActivity != null && mKey != null) {
                Map extraHeaders = new HashMap();
                extraHeaders.put(mKey, mValue);
                webView.loadUrl(url, extraHeaders);
            } else {
                webView.loadUrl(url);
            }

            return true;
        } else {
            mWebView.goBack();

            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (Exception var4) {
                Toast.makeText(mActivity, "未安装相应的客户端", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    }

}
