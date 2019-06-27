package com.baidu.carlife;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.baidu.carlife.camera.CameraSurfaceHolder;
import com.baidu.carlife.connect.CarlifeCmdMessage;
import com.baidu.carlife.connect.ConnectClient;
import com.baidu.carlife.message.MsgBaseHandler;
import com.baidu.carlife.message.MsgHandlerCenter;
import com.baidu.carlife.util.DigitalTrans;
import com.baidu.carlife.util.LogUtil;
import com.baidu.carlife.video.MediaModel;
import com.baidu.carlife.video.PlayState;
import com.baidu.carlife.video.PlayerEngineListener;
import com.baidu.carlife.video.VideoPlayEngineImpl;

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

        ConnectClient.getInstance().init(this);
        ConnectClient.getInstance().sendConnectStartBroadcast();
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
            LogUtil.d(TAG, "handleMessage get msg: " + DigitalTrans.algorismToHEXString(msg.what, 8));
            switch (msg.what) {
                case CommonParams.MSG_CONNECT_STATUS_CONNECTED:
//                    if (!mCameraSurfaceHolder.startCamara()) {
//                        MediaModel mediaModel = new MediaModel();
//                        mediaModel.setUrl("/sdcard/test.avi");
//                        startPlay(mediaModel);
//                    }
                    moveTaskToBack(true);
                    break;
                case CommonParams.MSG_CONNECT_STATUS_DISCONNECTED:
                    mCameraSurfaceHolder.stopCamara();
                    break;
            }
        }

        @Override
        public void careAbout() {
            addMsg(CommonParams.MSG_CONNECT_STATUS_CONNECTED);
            addMsg(CommonParams.MSG_CONNECT_STATUS_DISCONNECTED);
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

    public void startPlay(MediaModel mediaModel) {
        mPlayerEngineImpl.playMedia(mediaModel);
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

    private void dumpData(String tag, CarlifeCmdMessage carlifeMsg) {
        String msg = "";
        try {
            msg += "index = " + Integer.toString(carlifeMsg.getIndex());
            msg += ", length = " + Integer.toString(carlifeMsg.getLength());
            msg += ", service_type = 0x" + DigitalTrans.algorismToHEXString(carlifeMsg.getServiceType(), 8);
            msg += ", reserved = 0x" + DigitalTrans.algorismToHEXString(carlifeMsg.getReserved(), 8);
            msg += ", name = " + CommonParams.getMsgName(carlifeMsg.getServiceType());
            LogUtil.d(TAG, "[" + tag + "]" + msg);
        } catch (Exception e) {
            LogUtil.e(TAG, "dumpData get Exception");
            e.printStackTrace();
        }
    }
}
