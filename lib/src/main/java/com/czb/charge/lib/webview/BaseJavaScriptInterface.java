package com.czb.charge.lib.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Author: paitasuo
 * Date: 2018/12/20
 * Description:
 */
public class BaseJavaScriptInterface {
    private String key, value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //拿到设置webView的属性
    @JavascriptInterface
    public void setExtraInfoHead(String key, String value) {
        setKey(key);
        setValue(value);
        Log.e("添加头信息", key + "," + value);
    }
}

