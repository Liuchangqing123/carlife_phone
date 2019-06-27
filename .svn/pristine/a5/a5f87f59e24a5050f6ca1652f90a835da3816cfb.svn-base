package com.didi365.carlife.android.phone;

import android.app.Application;
import android.util.DisplayMetrics;

import com.didi365.carlife.android.phone.util.CarlifeUtil;
import com.didi365.carlife.android.phone.util.CommonParams;
import com.didi365.carlife.android.phone.util.LogUtil;

/**
 * Created by zheng on 2019/4/1
 */
public class CarlifePhoneApplication extends Application {

    private final String TAG = CarlifePhoneApplication.class.getSimpleName();

    public static int screenWidth = 1024;
    public static int screenHeight = 600;
    public static int frameRate = 25;

    @Override
    public void onCreate() {
        super.onCreate();
//        CommonParams.SD_DIR = "/sdcard" + "/" + CommonParams.SD_DIR_NAME;
//        LogUtil.e("CarlifePhoneApplication", CommonParams.SD_DIR);
//        CrashHandler.getInstance().init(getApplicationContext());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        float density = dm.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;
        LogUtil.e(TAG, screenWidth + " " + screenHeight);
        LogUtil.e(TAG, density + " " + densityDpi);
    }
}
