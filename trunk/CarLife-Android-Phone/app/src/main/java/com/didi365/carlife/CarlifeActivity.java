package com.didi365.carlife;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.didi365.carlife.camera.CameraSurfaceHolder;
import com.didi365.carlife.connect.ConnectClient;
import com.didi365.carlife.message.MsgBaseHandler;
import com.didi365.carlife.message.MsgHandlerCenter;
import com.didi365.carlife.util.LogUtil;
import com.didi365.carlife.video.MediaModel;
import com.didi365.carlife.video.PlayState;
import com.didi365.carlife.video.PlayerEngineListener;
import com.didi365.carlife.video.VideoPlayEngineImpl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CarlifeActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnTouchListener {

    private final String TAG = "CarlifeActivity";

    private SurfaceView surfaceView;
    private SurfaceHolder holder = null;

    private CameraSurfaceHolder mCameraSurfaceHolder = new CameraSurfaceHolder();

    private MsgBaseHandler mMainActivityHandler = null;

    private VideoPlayEngineImpl mPlayerEngineImpl;
    private VideoPlayEngineListener mPlayEngineListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        mCameraSurfaceHolder.setCameraSurfaceHolder(CarlifeActivity.this, surfaceView);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mPlayerEngineImpl = new VideoPlayEngineImpl(this, holder);
        mPlayEngineListener = new VideoPlayEngineListener();
        mPlayerEngineImpl.setPlayerListener(mPlayEngineListener);

        mMainActivityHandler = new MsgMainActivityHandler();
        MsgHandlerCenter.registerMessageHandler(mMainActivityHandler);
        startCarlifeActivity();

//        ConnectClient.getInstance().init(CarlifeActivity.this);
//        ConnectClient.getInstance().sendConnectStartBroadcast();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogUtil.d(TAG, "onTouch " + " event " + event);
        return false;
    }

    private class MsgMainActivityHandler extends MsgBaseHandler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.e(TAG, "handleMessage get msg: " + CommonParams.getMsgName(msg.what));
            switch (msg.what) {
                case CommonParams.MSG_CONNECT_STATUS_CONNECTED:
//                    if (!mCameraSurfaceHolder.startCamara()) {
//                        MediaModel mediaModel = new MediaModel();
//                        mediaModel.setUrl("/sdcard/test.avi");
//                        startPlay(mediaModel);
//                    }

                    moveTaskToBack(true);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    break;
                case CommonParams.MSG_CONNECT_STATUS_DISCONNECTED:
                    mCameraSurfaceHolder.stopCamara();
                    break;
                case CommonParams.MSG_ANDROID_DEBUG_CONNECT_STATUS_LISTENED:
                    ConnectClient.getInstance().init(CarlifeActivity.this);
                    ConnectClient.getInstance().sendConnectStartBroadcast();
                    break;
            }
        }

        @Override
        public void careAbout() {
            addMsg(CommonParams.MSG_CONNECT_STATUS_CONNECTED);
            addMsg(CommonParams.MSG_CONNECT_STATUS_DISCONNECTED);
            addMsg(CommonParams.MSG_ANDROID_DEBUG_CONNECT_STATUS_LISTENED);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectClient.getInstance().uninit();
        MsgHandlerCenter.unRegisterMessageHandler(mMainActivityHandler);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.e(TAG, "onTouchEvent " + " event " + event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConnectClient.getInstance().isCarlifeConnected()) {
            moveTaskToBack(true);
        }
    }

    private void startPlay(MediaModel mediaModel) {
        mPlayerEngineImpl.playMedia(mediaModel);
    }

    private void startCarlifeActivity() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.baidu.carlife", "com.baidu.carlife.CarlifeActivity");
        intent.setComponent(cn);
        if (intent.resolveActivityInfo(getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
            new Thread(new ListenerRunnable()).start();
        } else {
            LogUtil.d(TAG, "carlife app not installed!");
            MsgHandlerCenter.dispatchMessage(CommonParams.MSG_ANDROID_DEBUG_CONNECT_STATUS_LISTENED);
        }
    }

    private class VideoPlayEngineListener implements PlayerEngineListener {

        @Override
        public void onTrackPlay(MediaModel itemInfo) {
            PlayState.playRate = 1;
        }

        @Override
        public void onTrackStop(MediaModel itemInfo) {
            PlayState.playRate = 0;
            PlayState.playDuration = 0;
            PlayState.playPosition = 0;
        }

        @Override
        public void onTrackPause(MediaModel itemInfo) {
            PlayState.playRate = 0;
        }

        @Override
        public void onTrackPrepareSync(MediaModel itemInfo) {
        }

        @Override
        public void onTrackPrepareComplete(MediaModel itemInfo) {
            int duration = mPlayerEngineImpl.getDuration();
            PlayState.playDuration = duration / 1000;
        }

        @Override
        public void onTrackStreamError(MediaModel itemInfo) {
            mPlayerEngineImpl.stop();
        }

        @Override
        public void onTrackPlayComplete(MediaModel itemInfo) {
            startPlay(itemInfo);
        }
    }

    private class ListenerRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                String netStr = exec("netstat -an | grep 7240");
                LogUtil.e(TAG, "netStr==" + netStr);
                if (!TextUtils.isEmpty(netStr)) {
                    String[] tcp = netStr.split("\n");
                    boolean listen = false;
                    for (String item : tcp) {
                        if (item.contains(":7240") && item.contains("LISTEN")) {
                            listen = true;
                            MsgHandlerCenter.dispatchMessage(CommonParams.MSG_ANDROID_DEBUG_CONNECT_STATUS_LISTENED);
                            break;
                        }
                    }
                    if (listen) {
                        break;
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String exec(String str) {
        byte[] buffer = new byte[1024 * 5];
        String result = "";
        try {
            Process p = Runtime.getRuntime().exec(str);
            InputStream in = new DataInputStream(p.getInputStream());
            int count = 0;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((count = in.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, count);
            }
            result = byteArrayOutputStream.toString();
            return result;
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return result;
    }
}
