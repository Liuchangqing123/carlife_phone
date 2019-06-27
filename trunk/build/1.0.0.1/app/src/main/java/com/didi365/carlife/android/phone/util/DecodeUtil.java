package com.didi365.carlife.android.phone.util;

import com.didi365.carlife.android.phone.CarlifePhoneApplication;
import com.didi365.carlife.android.phone.connect.AOAAccessorySetup;
import com.didi365.carlife.android.phone.connect.ConnectManager;
import com.didi365.carlife.android.phone.encryption.AESManager;
import com.didi365.carlife.android.phone.encryption.EncryptSetupManager;

/**
 * Created by zheng on 2019/3/29
 */
public class DecodeUtil {

    private final String TAG = DecodeUtil.class.getSimpleName();

    private DecoderThread decoderThread = null;
    private static DecodeUtil mInstance = null;

    private final int LEN_OF_FRAME_HEAD = 4;
    private final int LEN_OF_MSG_HEAD = 8;

    private byte[] head = new byte[LEN_OF_MSG_HEAD];

    private byte[] SPS_BUFFER;

    private long count = -1;

    private boolean mIsDecorderReady;

    private int lenMsgHead = CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE;
    private int lenMsgData = -1;

    private AESManager mWriteAESManager = new AESManager();

    private boolean isRunning = false;

    private DecodeUtil() {
        System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_VIDEO), 0, head, 0, 4);
        byte[] decodeByte = new byte[LEN_OF_FRAME_HEAD + 12];
        System.arraycopy(ByteConvert.intToBytes(12), 0, decodeByte, 0, LEN_OF_FRAME_HEAD);
        System.arraycopy(ByteConvert.intToBytes(CarlifePhoneApplication.screenWidth), 0, decodeByte, 4, 4);
        System.arraycopy(ByteConvert.intToBytes(CarlifePhoneApplication.screenHeight), 0, decodeByte, 8, 4);
        System.arraycopy(ByteConvert.intToBytes(CarlifePhoneApplication.frameRate), 0, decodeByte, 12, 4);
        ConnectManager.getInstance().writeVideoData(decodeByte, decodeByte.length);

        ConnectManager.getInstance().startAudioSocket();
        AudioPlayerUtil.getInstance().startDecode();
    }

    public static DecodeUtil getInstance() {
        if (mInstance == null) {
            mInstance = new DecodeUtil();
        }
        return mInstance;
    }

    public void startDecode() {
        isRunning = true;
        mIsDecorderReady = false;
        if (decoderThread == null) {
            decoderThread = new DecoderThread();
            decoderThread.start();
        }
    }

    public void pauseDecode() {
        mIsDecorderReady = true;
    }

    public void stopDecode() {
        isRunning = false;
        decoderThread = null;
    }

    private class DecoderThread extends Thread {

        @Override
        public void run() {
            LogUtil.e(TAG, "START REC VIDEO RUN");
            byte inputData[] = new byte[1000000];
            while (isRunning) {
                count++;
                ConnectManager.getInstance().readVideoData(inputData, 0, LEN_OF_FRAME_HEAD);
                lenMsgData = ByteConvert.bytesToInt(new byte[]{inputData[0], inputData[1], inputData[2], inputData[3]});
                LogUtil.e(TAG, "lenMsgData " + lenMsgData);
                if (count == 0) {
                    SPS_BUFFER = new byte[lenMsgData];
                    ConnectManager.getInstance().readVideoData(SPS_BUFFER, 0, lenMsgData);
                    continue;
                } else if (count == 1) {
                    if (inputData.length < lenMsgHead + lenMsgData + SPS_BUFFER.length) {
                        inputData = new byte[lenMsgHead + lenMsgData + SPS_BUFFER.length];
                    }
                    System.arraycopy(SPS_BUFFER, 0, inputData, lenMsgHead, SPS_BUFFER.length);
                    ConnectManager.getInstance().readVideoData(inputData, lenMsgHead + SPS_BUFFER.length, lenMsgData);
                    lenMsgData += SPS_BUFFER.length;
                } else {
                    ConnectManager.getInstance().readVideoData(inputData, lenMsgHead, lenMsgData);
                }
                if (mIsDecorderReady) {
                    continue;
                }
                if (EncryptSetupManager.getInstance().isEncryptEnable()) {
                    byte[] encryptData = mWriteAESManager.encrypt(inputData, lenMsgHead, lenMsgData);
                    if (encryptData.length + lenMsgHead > inputData.length) {
                        inputData = new byte[encryptData.length + lenMsgHead];
                    }
                    System.arraycopy(encryptData, 0, inputData, lenMsgHead, encryptData.length);
                    lenMsgData = encryptData.length;
                }
                System.arraycopy(ByteConvert.intToBytes(lenMsgHead + lenMsgData), 0, head, LEN_OF_FRAME_HEAD, LEN_OF_FRAME_HEAD);
                System.arraycopy(ByteConvert.intToBytes(lenMsgData), 0, inputData, 0, LEN_OF_FRAME_HEAD);
                System.arraycopy(ByteConvert.longToBytesLowbyte(System.currentTimeMillis()), 0, inputData, LEN_OF_FRAME_HEAD, LEN_OF_FRAME_HEAD);
                System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_VIDEO_DATA), 0, inputData, LEN_OF_MSG_HEAD, LEN_OF_FRAME_HEAD);

                AOAAccessorySetup.getInstance().bulkTransferOut(head, LEN_OF_MSG_HEAD, inputData, lenMsgHead + lenMsgData);
            }
        }
    }
}
