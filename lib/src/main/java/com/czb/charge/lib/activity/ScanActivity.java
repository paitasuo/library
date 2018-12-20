package com.czb.charge.lib.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.czb.charge.lib.R;
import com.czb.charge.lib.comm.base.BaseAct;
import com.czb.charge.lib.scan.camera.CameraManager;
import com.czb.charge.lib.scan.decode.DecodeThread;
import com.czb.charge.lib.scan.utils.BeepManager;
import com.czb.charge.lib.scan.utils.CaptureActivityHandler;
import com.czb.charge.lib.scan.utils.InactivityTimer;
import com.czb.charge.lib.comm.utils.CameraPermissionCompat;
import com.google.zxing.Result;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 * Author: zqz
 * Date:
 * Description:
 */
public class ScanActivity extends BaseAct implements SurfaceHolder.Callback {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private boolean isPause = false;
    private Camera camera;
    private CaptureActivityHandler handler;
    private Rect mCropRect = null;
    private CameraManager cameraManager;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private ObjectAnimator objectAnimator;

    private SurfaceView capturePreview;
    private RelativeLayout captureContainer;
    private RelativeLayout captureCropView;
    private LinearLayout flashLayout;
    private ImageView scanLine;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;
    private boolean isflash = false;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!CameraPermissionCompat.checkCameraPermission(this, null)) {
            Toast.makeText(this, "摄像头启动失败，请尝试在手机应用权限管理中打开权限", Toast.LENGTH_LONG).show();
            return;
        }
        initData();
    }

    protected void initData() {
        capturePreview = findViewById(R.id.capture_preview);
        captureCropView = findViewById(R.id.capture_crop_view);
        flashLayout = findViewById(R.id.flashLayout);
        scanLine = findViewById(R.id.scan_line);
        captureContainer = findViewById(R.id.capture_container);

        //闪光灯控制
        flashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isflash) {
                    closeFlashlight();
                    isflash = false;
                } else {
                    openFlashlight();
                    isflash = true;
                }
            }
        });


        //扫描线动画1(属性动画可暂停)
        float curTranslationY = scanLine.getTranslationY();
        objectAnimator = ObjectAnimator.ofFloat(scanLine, "translationY", curTranslationY, dp2px(this, 170));
        objectAnimator.setDuration(4000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }


    @Override
    public void onResume() {
        super.onResume();
        startScan();
    }

    @Override
    public void onPause() {
        pauseScan();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        if (objectAnimator != null) {
            objectAnimator.end();
        }
        super.onDestroy();
    }



    /**
     * 开始扫码
     */
    private void startScan() {
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        if (isPause) {
            objectAnimator.start();
            isPause = false;
        } else {
            objectAnimator.start();
        }

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());
        handler = null;
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(capturePreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            capturePreview.getHolder().addCallback(this);
        }
        inactivityTimer.onResume();
    }

    /**
     * 暂停扫码
     */
    private void pauseScan() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            capturePreview.getHolder().removeCallback(this);
        }
        objectAnimator.end();
        isPause = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 扫码成功回调方法
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        pauseScan();
//        loadData(rawResult.toString());
    }

    /**
     * 初始化相机
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 相机打开出错弹框
     */
    private void displayFrameworkBugMessageAndExit() {
        // camera error
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getString(R.string.app_name));
//        builder.setMessage("相机打开出错，请稍后重试");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//
//        });
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                finish();
//            }
//        });
//        builder.show();
    }


    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        captureCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = captureCropView.getWidth();
        int cropHeight = captureCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = captureContainer.getWidth();
        int containerHeight = captureContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static float dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }


    //打开闪光灯
    private void openFlashlight() {
        camera = cameraManager.getCamera();
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
        }
    }

    //关闭闪光灯
    private void closeFlashlight() {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.startPreview();
        }

    }

    /**
     * 中文乱码处理
     *
     * @param str
     * @return
     */
    private String recode(String str) {
        String formart = "";
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
            } else {
                formart = str;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }

//    //网络请求成功
//    @Override
//    public void onReqSuccess(Object result, int code) {
//        switch (code) {
//            case RequestCode.C_GET_QRCODE:
//                Bundle bundle = new Bundle();
//                bundle.putString("result", result.toString());
//                startActivityWithExtras(ChargeSelectGunActivity.class, bundle);
//                break;
//        }
//    }
//
//    @Override
//    public void onReqFailed(String errorMsg, int code) {
//        switch (code) {
//            case RequestCode.C_GET_QRCODE:
//                startScan();
//                MessageCode messageCode = GsonTool.parseJson(errorMsg, MessageCode.class);
//                if (messageCode == null || messageCode.getMessage() == null) return;
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("错误类型", messageCode.getMessage());
//                    ZhugeSDK.getInstance().track(this, "扫码失败", jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }


    /**
     * 请求数据
     //     */
//    private void loadData(String scanQRUrl) {
//        if (TextUtils.isEmpty(scanQRUrl)) {
//            showToastError("扫码失败");
//            startScan();
//            return;
//        }
//        MyApplication.getInstance().showDialog(this, "正在获取数据，请稍后");
//        HashMap hashMap = new HashMap();
//        hashMap.put("scanQRUrl", scanQRUrl);
//        hashMap.put("czbOperatorId", "1");
//        RequestManager.getInstance(this).requestAsyn(this, Url.C_GET_QRCODE,
//                RequestManager.TYPE_POST_FORM, hashMap, this, RequestCode.C_GET_QRCODE);
//    }

}
