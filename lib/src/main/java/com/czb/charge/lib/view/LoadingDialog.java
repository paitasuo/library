package com.czb.charge.lib.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.czb.charge.lib.R;


/**
 * Created by czb365 on 2017/7/6.
 */

public class LoadingDialog extends Dialog {
    private TextView tv_text;
    private Context context;
    private static LoadingDialog loadingDialog;

//    public LoadingDialog(Context context){
//        super(context);
//        this.context=context;
//    }

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.dialog);
        this.context = context;
        //去阴影
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        /**设置对话框背景透明*/
//        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.dialog_loading);
        tv_text = findViewById(R.id.text);
        if (!TextUtils.isEmpty(msg)) tv_text.setText(msg);
        else tv_text.setText("数据加载中，请稍后");
        setCanceledOnTouchOutside(false);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public LoadingDialog setMessage(String message) {
        if (context == null) return null;
        if (context instanceof Activity && ((Activity) context).isFinishing()) return null;

        tv_text.setText(message);
        this.show();
        return this;
    }


    //自定义提示消息
    public static void showDialog(Context context, String str) {
        if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
        loadingDialog = new LoadingDialog(context,str);
    }

    //默认消息提示
    public static void showDialog(Context context) {
        try {
            if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
            loadingDialog = new LoadingDialog(context,"数据加载中，请稍后");
        } catch (Exception e) {

        }
    }

    public static void dimissDialog() {
        try {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }


}
