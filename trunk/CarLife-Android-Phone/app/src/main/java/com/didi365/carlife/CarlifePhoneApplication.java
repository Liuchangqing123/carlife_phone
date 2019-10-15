package com.didi365.carlife;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.didi365.carlife.util.LogUtil;

/**
 * Created by zheng on 2019/4/1
 */
public class CarlifePhoneApplication extends Application {

    private final String TAG = CarlifePhoneApplication.class.getSimpleName();

    public static int screenWidth = 1280;
    public static int screenHeight = 720;
    public static int frameRate = 30;

    @Override
    public void onCreate() {
        super.onCreate();
        CommonParams.SD_DIR = "/sdcard" + "/" + CommonParams.SD_DIR_NAME;
        CrashHandler.getInstance().init(getApplicationContext());

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        float density = dm.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;
        LogUtil.e(TAG, screenWidth + " " + screenHeight);
        LogUtil.e(TAG, density + " " + densityDpi);
    }
}
