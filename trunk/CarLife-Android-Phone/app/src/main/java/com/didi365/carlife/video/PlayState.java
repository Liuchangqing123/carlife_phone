package com.didi365.carlife.video;

import android.util.Log;

public class PlayState {

    public static final int MPS_NOFILE = -1;

    public static final int MPS_INVALID = 0;

    public static final int MPS_PLAYING = 1;

    public static final int MPS_PAUSE = 2;

    public static final int MPS_STOP = 3;

    public static final int MPS_PARESYNC = 4;

    public static final int MPS_PARECOMPLETE = 5;

    public static int playRate;

    public static int playPosition;

    public static int playDuration;

    public static boolean isSeekbarTouch;

    public static String TV_AIRPLAY = "tv_airplay";

    public static String TV_ANDROID_USB = "tv_android_usb";

    public static String TV_ANDROID_WIFI = "tv_android_wifi";

    public static int getPlaybackRate() {
        if (isSeekbarTouch) {
            return 0;
        }
        Log.v("VideoPlayFragment2", "getPlaybackRate " + playRate);
        return playRate;
    }

    public static int getPlaybackPosition() {
        Log.v("VideoPlayFragment2", "getPlaybackPosition " + playPosition);
        return playPosition;
    }

    public static int getPlaybackDuartion() {
        Log.v("VideoPlayFragment2", "getPlaybackDuartion " + playDuration);
        return playDuration;
    }
}