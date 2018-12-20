package com.czb.charge.lib.webview;

import android.view.View;
import android.widget.ProgressBar;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Author: paitasuo
 * Date: 2018/12/19
 * Description:
 */
public class MyWebChromeClient extends WebChromeClient {
    private ProgressBar mProgressBar;

    public MyWebChromeClient(ProgressBar progressBar) {
        mProgressBar = progressBar;

    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback
            customViewCallback) {
        super.onShowCustomView(view, customViewCallback);
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
    }

    @Override
    public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
        return super.onJsAlert(webView, s, s1, jsResult);
    }

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
    }

    @Override
    public void onProgressChanged(WebView webView, int progress) {
        super.onProgressChanged(webView, progress);
        if (progress == 100) {
            mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
        } else {
            mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
            mProgressBar.setProgress(progress);//设置进度值
        }

    }
}
