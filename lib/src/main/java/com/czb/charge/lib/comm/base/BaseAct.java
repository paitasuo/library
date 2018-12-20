package com.czb.charge.lib.comm.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.czb.charge.lib.R;
import com.czb.charge.lib.comm.AppManager;


/**
 * Author: paitasuo
 * Date: 2018/11/5
 * Description:
 */
public abstract class BaseAct extends AppCompatActivity  {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        mContext = this;
        addToAppManager();
        setContentView(getContentLayoutId());

        handleBundle();//参数处理
        init(savedInstanceState);

    }


    private void addToAppManager() {
        AppManager.getAppManager().addActivity(this);
    }


    /**
     * 如果有屏幕长亮等需要在super.onCreate()之前调用的方法, 则重新此方法
     *
     * @param savedInstanceState Bundle savedInstanceState
     */
    protected void beforeCreate(Bundle savedInstanceState) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        judgeOverridePendingTransition(false);
    }



    private void handleBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            handleBundle(bundle);
        }
    }

    protected void intentJump(Class clazz) {
        intentJump(clazz, null);
    }

    protected void intentJump(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
        judgeOverridePendingTransition(true);

    }

    protected void intentJump(Intent intent) {
        startActivity(intent);
        judgeOverridePendingTransition(true);
    }


    protected void judgeOverridePendingTransition(boolean isIn) {
        if (isIn) {
            if (useDefOverrPendingTran())
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            if (useDefOverrPendingTran())
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }


    }

    protected void intentJumpForResult(Class<?> clazz, int requestCode) {
        intentJumpForResult(clazz, requestCode, null);
    }

    protected void intentJumpForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        judgeOverridePendingTransition(true);

    }

    /**
     * 将要打开activity的进入方向
     *
     * @return
     */
    protected boolean useDefOverrPendingTran() {
        return true;
    }

    protected abstract int getContentLayoutId();

    /**
     * activity之间传递参数处理
     *
     * @param bundle
     */
    protected void handleBundle(Bundle bundle) {
    }

    /**
     * 初始化相关操作
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);


}
