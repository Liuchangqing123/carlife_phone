
package com.baidu.carlife.connect;

import com.baidu.carlife.util.ByteConvert;
import com.baidu.carlife.CommonParams;
import com.baidu.carlife.util.LogUtil;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectHeartBeat {
    private static final String TAG = "ConnectHeartBeat";
    public static final int HEART_BEAT_CHECK_MS = 1000;
    private final int LEN_OF_MSG_HEAD = 8;
    private final int LEN_OF_MSG_DATA = 12;
    private byte[] head = new byte[LEN_OF_MSG_HEAD];

    private static ConnectHeartBeat mInstance = null;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private byte[] packageHead = new byte[12];
    private boolean isRunning = false;

    private ConnectHeartBeat() {
        System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_VIDEO), 0, head, 0, 4);
        System.arraycopy(ByteConvert.intToBytes(LEN_OF_MSG_DATA), 0, head, 4, 4);
    }

    public static ConnectHeartBeat getInstance() {
        if (null == mInstance) {
            synchronized (ConnectHeartBeat.class) {
                if (null == mInstance) {
                    mInstance = new ConnectHeartBeat();
                }
            }
        }
        return mInstance;
    }

    /**
     * An empty packet is sent from the video channel as a heartbeat,
     * containing a timestamp, while return -1 indicates that the channel has been disconnected
     */
    public int sendEmptyPacket() {
        long timeStamp = System.currentTimeMillis();
        packageHead[0] = 0;
        packageHead[1] = 0;
        packageHead[2] = 0;
        packageHead[3] = 0;
        packageHead[4] = (byte) ((timeStamp & 0x7f000000) >> 24);
        packageHead[5] = (byte) ((timeStamp & 0xff0000) >> 16);
        packageHead[6] = (byte) ((timeStamp & 0xff00) >> 8);
        packageHead[7] = (byte) ((timeStamp & 0xff) >> 0);
        packageHead[8] = 0;
        packageHead[9] = 2;
        packageHead[10] = 0;
        packageHead[11] = 2;

        if (ConnectManager.getInstance().getConnectType() == ConnectManager.CONNECTED_BY_AOA) {
            return AOAAccessorySetup.getInstance().bulkTransferOut(head, LEN_OF_MSG_HEAD, packageHead, LEN_OF_MSG_DATA);
        } else if (ConnectManager.getInstance().getConnectType() == ConnectManager.CONNECTED_BY_USBMUXD) {
            return UsbMuxdConnectManager.getInstance().writeAudioData(packageHead, LEN_OF_MSG_DATA);
        }
        return -1;
    }

    public void startConnectHeartBeatTimer() {
        try {
            LogUtil.d(TAG, "start Connect heart beat timer");
            if (mTimer == null) {
                mTimer = new Timer();
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (sendEmptyPacket() < 0) {
                            LogUtil.d(TAG, "ean connect fail,send hear beat packet fail");
                        }
                    }
                };
                mTimer.schedule(mTimerTask, HEART_BEAT_CHECK_MS, HEART_BEAT_CHECK_MS);
            }
        } catch (Exception ex) {
            LogUtil.d(TAG, "startTimer get exception");
            ex.printStackTrace();
        }
    }

    public void stopConnectHeartBeatTimer() {
        LogUtil.d(TAG, "stop Connect heart beat timer");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
}
