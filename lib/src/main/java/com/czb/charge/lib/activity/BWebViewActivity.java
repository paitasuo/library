package com.czb.charge.lib.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.czb.charge.lib.R;
import com.czb.charge.lib.comm.base.BaseAct;
import com.czb.charge.lib.comm.base.BaseHelper;
import com.czb.charge.lib.comm.UrlCons;
import com.czb.charge.lib.view.X5WebView;
import com.czb.charge.lib.webview.BaseJavaScriptInterface;
import com.czb.charge.lib.webview.MyJavaScriptInterface;
import com.czb.charge.lib.webview.MyJavaScriptInterfaceDefault;
import com.czb.charge.lib.webview.MyWebChromeClient;
import com.czb.charge.lib.webview.MyWebViewClient;
import com.czb.charge.lib.webview.MyWebViewClientDefault;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Author: paitasuo
 * Date: 2018/12/19
 * Description:
 */
public class BWebViewActivity extends BaseAct {
    private X5WebView mWebView;
    private ProgressBar mProgressBar;
    private BaseJavaScriptInterface mInterface;
    private MyWebChromeClient mChromeClient;
    private WebViewClient mWebViewClient;
    private String mKey;
    private String mValue;
    private int mNavigationType;

    @Override
    protected void handleBundle(Bundle bundle) {
        super.handleBundle(bundle);
        mNavigationType = bundle.getInt("type");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_base_webview;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mWebView = findViewById(R.id.webView);
        mProgressBar = findViewById(R.id.progress);
        mWebView.loadUrl(UrlCons.WEB_HOME);
        if (mNavigationType == 1) {
            mInterface = new MyJavaScriptInterfaceDefault(this);
            mWebViewClient = new MyWebViewClientDefault(this, mWebView, mKey, mValue);
        }
        if (mNavigationType == 2) {
            mInterface = new MyJavaScriptInterface(this, BaseHelper.getListener());
            mWebViewClient = new MyWebViewClient(this, mWebView, mKey, mValue);
        }
        mChromeClient = new MyWebChromeClient(mProgressBar);
        mKey = mInterface.getKey();
        mValue = mInterface.getValue();
        mWebView.addJavascriptInterface(mInterface, "czb"); //第二个参数不可改

        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mWebViewClient);


    }

    //返回键监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //返回上一级
    public void goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
