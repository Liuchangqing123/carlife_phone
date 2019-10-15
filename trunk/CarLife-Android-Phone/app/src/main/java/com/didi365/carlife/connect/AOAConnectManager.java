package com.didi365.carlife.connect;

import android.content.Context;
import android.os.SystemClock;

import com.didi365.carlife.encryption.AESManager;
import com.didi365.carlife.encryption.EncryptSetupManager;
import com.didi365.carlife.logic.CarlifeDeviceInfoManager;
import com.didi365.carlife.model.ModuleStatusModel;
import com.didi365.carlife.util.ByteConvert;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.util.CarlifeUtil;
import com.didi365.carlife.util.DigitalTrans;
import com.didi365.carlife.util.LogUtil;
import com.didi365.carlife.vehicle.CarDataManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zheng on 2019/3/29
 */
public class AOAConnectManager {

    private final String TAG = AOAConnectManager.class.getSimpleName();

    private static final String AOA_CONNECT_THREAD_NAME = "AOAConnectThread";
    private static final String AOA_READ_THREAD_NAME = "AOAReadThread";
    private static final String SOCKET_READ_THREAD_NAME = "SocketReadThread";
    private static final int LEN_OF_MSG_HEAD = 8;
    private static final int SLEEP_TIME_MS = 500;

    private static final int SEND_BUFFER_SIZE = 320 * 1024;
    private static final int RECEIVE_BUFFER_SIZE = 320 * 1024;

    private static final int AOA_MAX_BYTES = 64 * 1024 * 1024;

    public static long mTimeConnectStart = 0;

    private static AOAConnectManager mInstance = null;
    private Context mContext;

    private AOAConnectThread mAOAConnectThread = null;
    private AOAReadThread mAOAReadThread = null;

    private SocketReadThread mSocketReadThread = null;
    private SocketReadThread mSocketReadVideoThread = null;
    private SocketReadThread mSocketReadAudioVRThread = null;
    private SocketReadThread mSocketReadMiudriveThread = null;
    private SocketReadThread mSocketReadMiudriveVRThread = null;

    private AESManager mWriteAESManager = new AESManager();

    private String audio = "/sdcard/test.pcm";

    private FileOutputStream fileOutputStream;

    private AOAConnectManager() {
        try {
            fileOutputStream = new FileOutputStream(audio);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static AOAConnectManager getInstance() {
        if (null == mInstance) {
            synchronized (AOAConnectManager.class) {
                if (null == mInstance) {
                    mInstance = new AOAConnectManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        LogUtil.e(TAG, "init");
        mContext = context;
        AOAAccessorySetup.getInstance().init(mContext);
    }

    public void unInit() {
        LogUtil.e(TAG, "unInit");
        stopAOAReadThread();
        stopSocketReadThread();
        AOAAccessorySetup.getInstance().unInit();
    }

    public int writeCarlifeCmdMessage(CarlifeCmdMessage msg) {
        if (null == mSocketReadThread) {
            LogUtil.e(TAG, "write error: mSocketReadThread is null");
            return -1;
        }
        return mSocketReadThread.writeData(msg);
    }

    public void writeVideoData(byte[] data, int len) {
        if (mSocketReadVideoThread != null) {
            mSocketReadVideoThread.writeData(data, 0, len);
        }
    }

    public void writeMiuCmdMessage(byte[] data, int len) {
        if (mSocketReadMiudriveThread != null) {
            mSocketReadMiudriveThread.writeData(data, 0, len);
        }
    }

    public void writeMiuVRMessage(byte[] data, int len) {
        if (mSocketReadMiudriveVRThread != null) {
            if (mSocketReadMiudriveVRThread.mOutputStream != null) {
                if (mSocketReadMiudriveVRThread.writeData(data, 0, len) == -1) {
                    CarlifeDeviceInfoManager.getInstance().sendMicRecordEnd();
                    mSocketReadMiudriveVRThread.reset();
                }
            }
        }
    }

    public void startSocketReadThread() {
        try {
            mSocketReadThread = new SocketReadThread(CommonParams.SOCKET_LOCALHOST_PORT, CommonParams.SERVER_SOCKET_NAME);
            mSocketReadThread.start();

            mSocketReadAudioVRThread = new SocketReadThread(CommonParams.SOCKET_AUDIO_VR_LOCALHOST_PORT, CommonParams.SERVER_SOCKET_AUDIO_VR_NAME);
            mSocketReadAudioVRThread.start();

            mSocketReadVideoThread = new SocketReadThread(CommonParams.SOCKET_VIDEO_LOCALHOST_PORT, CommonParams.SERVER_SOCKET_VIDEO_NAME);
            mSocketReadVideoThread.start();

            mSocketReadMiudriveThread = new SocketReadThread(CommonParams.SOCKET_MIUDRIVE_PORT, CommonParams.SERVER_SOCKET_MIUDRIVE_NAME);
            mSocketReadMiudriveThread.start();

            mSocketReadMiudriveVRThread = new SocketReadThread(CommonParams.SOCKET_MIUDRIVE_VR, CommonParams.SERVER_SOCKET_MIUDRIVE_VR_NAME);
            mSocketReadMiudriveVRThread.start();
        } catch (Exception e) {
            LogUtil.e(TAG, "Start SocketRead Thread Fail");
            e.printStackTrace();
        }
    }

    public void stopSocketReadThread() {
        try {
            if (null != mSocketReadThread) {
                mSocketReadThread.cancel();
                mSocketReadThread = null;
            }

            if (null != mSocketReadVideoThread) {
                mSocketReadVideoThread.cancel();
                mSocketReadVideoThread = null;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Stop SocketRead Thread Fail");
            e.printStackTrace();
        }
    }

    public void startAOAConnectThread() {
        try {
            mAOAConnectThread = new AOAConnectThread();
            mAOAConnectThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAOAConnectThread() {
        LogUtil.e(TAG, "stopAOAConnectThread");
        try {
            mAOAConnectThread.cancel();
            mAOAConnectThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAOAReadThread() {
        try {
            mAOAReadThread = new AOAReadThread();
            mAOAReadThread.setName("AOAReadThread");
            mAOAReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAOAReadThread() {
        try {
            if (mAOAReadThread != null) {
                mAOAReadThread.cancel();
                mAOAReadThread = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AOAConnectThread extends Thread {
        private boolean isRunning = false;

        public AOAConnectThread() {
            LogUtil.e(TAG, "AOAConnectThread Created");
            isRunning = true;
            setName(AOA_CONNECT_THREAD_NAME);
        }

        public void cancel() {
            isRunning = false;
            LogUtil.e(TAG, "AOAConnectThread cancel isRunning " + isRunning);
        }

        @Override
        public void run() {
            isRunning = true;
            LogUtil.e(TAG, "Begin to connect carlife by AOA " + isRunning);
            try {
                while (true) {
                    if (!isRunning) {
                        LogUtil.e(TAG, "Carlife Connect Cancled");
                        return;
                    }
                    mTimeConnectStart = SystemClock.elapsedRealtime();
                    if (AOAAccessorySetup.getInstance().scanUsbDevices()) {
                        LogUtil.e(TAG, "Carlife AOA connect successed");
                        break;
                    }
                    sleep(SLEEP_TIME_MS);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "Exception when connect carlife by AOA " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private class AOAReadThread extends Thread {
        private boolean isRunning = false;

        private byte[] msg = new byte[LEN_OF_MSG_HEAD];
        private byte[] msgHead = new byte[LEN_OF_MSG_HEAD];
        private int typeMsg = -1;
        private int lenMsg = -1;
        private int ret = -1;

        public AOAReadThread() {
            LogUtil.e(TAG, "AOAReadThread Created");
            setName(AOA_READ_THREAD_NAME);
        }

        public void cancel() {
            isRunning = false;
        }

        @Override
        public void run() {
            isRunning = true;
            LogUtil.e(TAG, "Begin to read data by AOA");
            try {
                while (isRunning) {
                    if (!isRunning) {
                        LogUtil.e(TAG, "read data cancled");
                        return;
                    }
                    ret = AOAAccessorySetup.getInstance().bulkTransferIn(msgHead, LEN_OF_MSG_HEAD);
                    if (ret < 0) {
                        LogUtil.e(TAG, "bulkTransferIn fail 1");
                        break;
                    } else if (ret == 0) {
                        continue;
                    }
                    typeMsg = ByteConvert.bytesToInt(new byte[]{msgHead[0], msgHead[1], msgHead[2], msgHead[3]});
                    lenMsg = ByteConvert.bytesToInt(new byte[]{msgHead[4], msgHead[5], msgHead[6], msgHead[7]});
                    if (typeMsg < 1 || typeMsg > 6 || lenMsg < 8 || lenMsg > AOA_MAX_BYTES) {
                        LogUtil.e(TAG, "typeMsg or lenMsg is error");
                        break;
                    }
                    msg = new byte[LEN_OF_MSG_HEAD];
                    if (msg.length < lenMsg) {
                        msg = new byte[lenMsg];
                    }

                    if (AOAAccessorySetup.getInstance().bulkTransferIn(msg, lenMsg) < 0) {
                        LogUtil.e(TAG, "bulkTransferIn fail 2");
                        break;
                    }
                    switch (typeMsg) {
                        case CommonParams.MSG_CHANNEL_ID:
                            ConnectManager.getInstance().writeCmdData(msg, lenMsg);
                            ConnectManager.getInstance().writeAndroidDebugData(msg, lenMsg);
                            break;
                        case CommonParams.MSG_CHANNEL_ID_VIDEO:
                            break;
                        case CommonParams.MSG_CHANNEL_ID_AUDIO:
                            break;
                        case CommonParams.MSG_CHANNEL_ID_AUDIO_TTS:
                            break;
                        case CommonParams.MSG_CHANNEL_ID_AUDIO_VR:
                            ConnectManager.getInstance().writeAudioVRData(msg, lenMsg);
                            break;
                        case CommonParams.MSG_CHANNEL_ID_TOUCH:
                            CarlifeCmdMessage carlifeTouchMsg = new CarlifeCmdMessage(false);
                            carlifeTouchMsg.fromByteArray(msg, lenMsg);
                            DigitalTrans.dumpData(TAG, "RECV CarlifeTouchMessage", carlifeTouchMsg, false);
                            ConnectManager.getInstance().writeCarlifeTouchMessage(carlifeTouchMsg);
                            break;
                        default:
                            LogUtil.e(TAG, "AOAReadThread typeMsg error");
                            break;
                    }
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "Exception when read data by AOA");
                e.printStackTrace();
            }
        }
    }

    public void carlifeMiudriveMsgProtocol(CarlifeMiuCmdMessage carlifeMiuCmdMessage) {
        if (carlifeMiuCmdMessage.getReserved() == CommonParams.MIU_MSG_CMD_TYPE_FIX) {
            switch (carlifeMiuCmdMessage.getServiceType()) {
                case CommonParams.MIU_MSG_CMD_VOICE_UP:
                    byte[] vrInitByte = new byte[CommonParams.MIU_MSG_CMD_HEAD_SIZE + 2];
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MIU_MSG_CMD_TYPE_FIX), 0, vrInitByte, 0, 4);
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MIU_MSG_CMD_EVENT_MIC_SUCCESS), 3, vrInitByte, 4, 1);
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.SOCKET_MIUDRIVE_VR), 2, vrInitByte, CommonParams.MIU_MSG_CMD_HEAD_SIZE, 2);
                    vrInitByte[0] = (byte) (vrInitByte.length - 1);
                    writeMiuCmdMessage(vrInitByte, vrInitByte.length);
//                    if (mSocketReadMiudriveVRThread.mOutputStream != null) {
//                        CarlifeDeviceInfoManager.getInstance().sendMicRecordWakeUpStart();
//                        CarlifeDeviceInfoManager.getInstance().sendMicRecordRecogStart();
//                    }
                    break;
                case CommonParams.MIU_MSG_CMD_VOICE_DOWN:
                    if (mSocketReadMiudriveVRThread != null) {
                        mSocketReadMiudriveVRThread.reset();
                    }
//                    CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_VR_MODULE_ID, ModuleStatusModel.VR_STATUS_IDLE);
                    CarlifeDeviceInfoManager.getInstance().sendMicRecordEnd();
                    break;
                case CommonParams.MIU_MSG_CMD_MAP_IN:
                    CarDataManager.getInstance().requestCarVehicle(CarDataManager.MODULE_GPS_DATA, 1, 1);
                    break;
                case CommonParams.MIU_MSG_CMD_MAP_OUT:
                    break;
                case CommonParams.MIU_MSG_CMD_MEDIA_PLAY:
//                    CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_MUSIC_MODULE_ID, ModuleStatusModel.MUSIC_STATUS_RUNNING);
                    break;
                case CommonParams.MIU_MSG_CMD_MEDIA_PAUSE:
                    //CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_MUSIC_MODULE_ID, ModuleStatusModel.MUSIC_STATUS_IDLE);
                    break;
            }
        }
    }

    private class SocketReadThread extends Thread {
        private ServerSocket mServerSocket = null;
        private boolean isRunning = false;
        private int mSocketPort = -1;
        private String mSocketName = null;
        private String mThreadName = null;

        private Socket mSocket = null;
        private BufferedInputStream mInputStream = null;
        private BufferedOutputStream mOutputStream = null;

        private int lenMsgHead = -1;
        private int lenMsgData = -1;

        private byte[] msg = new byte[CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE];
        private byte[] head = new byte[LEN_OF_MSG_HEAD];

        public SocketReadThread(int port, String name) {
            try {
                mThreadName = name + SOCKET_READ_THREAD_NAME;
                setName(mThreadName);
                mSocketPort = port;
                mSocketName = name;
                LogUtil.e(TAG, "Create " + mThreadName + " " + mSocketPort);

                mServerSocket = new ServerSocket(mSocketPort);
                isRunning = true;

                if (mSocketName.equals(CommonParams.SERVER_SOCKET_NAME)) {
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID), 0, head, 0, 4);
                } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_VIDEO_NAME)) {
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_VIDEO), 0, head, 0, 4);
                } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_AUDIO_NAME)) {
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_AUDIO), 0, head, 0, 4);
                } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_AUDIO_TTS_NAME)) {
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_AUDIO_TTS), 0, head, 0, 4);
                } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_AUDIO_VR_NAME)) {
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_AUDIO_VR), 0, head, 0, 4);
                } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_TOUCH_NAME)) {
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_TOUCH), 0, head, 0, 4);
                } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_MIUDRIVE_VR_NAME)) {
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "Create " + mThreadName + " fail " + e.getMessage());
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                if (null != mServerSocket) {
                    mServerSocket.close();
                }
                if (null != mSocket) {
                    mSocket.close();
                    mSocket = null;
                }
                if (null != mInputStream) {
                    mInputStream.close();
                    mInputStream = null;
                }
                if (null != mOutputStream) {
                    mOutputStream.close();
                    mOutputStream = null;
                }
                isRunning = false;
            } catch (Exception e) {
                LogUtil.e(TAG, "Close " + mThreadName + " fail");
                e.printStackTrace();
            }
        }

        public void reset() {
            try {
                if (null != mSocket) {
                    mSocket.close();
                    mSocket = null;
                }
                if (null != mInputStream) {
                    mInputStream.close();
                    mInputStream = null;
                }
                if (null != mOutputStream) {
                    mOutputStream.close();
                    mOutputStream = null;
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "Reset " + mThreadName + " fail");
                e.printStackTrace();
            }
        }

        public int readData(byte[] buffer, int offset, int len) {
            int r = -1;
            try {
                if (null != mInputStream) {
                    int cnt;
                    cnt = len;
                    int dataLen = 0;
                    while (cnt > 0) {
                        r = mInputStream.read(buffer, offset + dataLen, cnt);
                        if (r > 0) {
                            cnt -= r;
                            dataLen += r;
                        } else {
                            LogUtil.e(TAG, mSocketName + " Receive Data Error: ret = " + r);
                            throw new IOException();
                        }
                    }
                    if (dataLen != len) {
                        LogUtil.e(TAG, mSocketName + " Receive Data Error: dataLen = " + dataLen);
                        throw new IOException();
                    }
                    return dataLen;
                } else {
                    LogUtil.e(TAG, mSocketName + " Receive Data Fail, mInputStream is null");
                    throw new IOException();
                }
            } catch (Exception e) {
                LogUtil.e(TAG, mSocketName + " IOException, Receive Data Fail");
                e.printStackTrace();
                return r;
            }
        }

        public int writeData(CarlifeCmdMessage msg) {
            try {
                if (null != mOutputStream) {
                    DigitalTrans.dumpData(TAG, "SEND CarlifeMsg CMD", msg, false);
                    if (EncryptSetupManager.getInstance().isEncryptEnable() && msg.getLength() > 0) {
                        byte[] encryptData = mWriteAESManager.encrypt(msg.getData(), 0, msg.getData().length);
                        if (encryptData == null) {
                            LogUtil.e(TAG, "encrypt failed!");
                            return -1;
                        }
                        msg.setLength(encryptData.length);
                        mOutputStream.write(msg.toByteArray());
                        mOutputStream.flush();
                        if (msg.getLength() > 0) {
                            mOutputStream.write(encryptData);
                            mOutputStream.flush();
                        }
                    } else {
                        mOutputStream.write(msg.toByteArray());
                        mOutputStream.flush();
                        if (msg.getLength() > 0) {
                            mOutputStream.write(msg.getData());
                            mOutputStream.flush();
                        }
                    }
                    return CommonParams.MSG_CMD_HEAD_SIZE_BYTE + msg.getLength();
                } else {
                    LogUtil.e(TAG, mSocketName + " Send Data Fail, mOutputStream is null");
                    throw new IOException();
                }
            } catch (Exception e) {
                LogUtil.e(TAG, mSocketName + " IOException, Send Data Fail " + e.getMessage());
                ConnectClient.getInstance().setIsConnected(false);
                e.printStackTrace();
                return -1;
            }
        }

        public int writeData(byte[] buffer, int offset, int len) {
            try {
                if (null != mOutputStream) {
                    mOutputStream.write(buffer, offset, len);
                    mOutputStream.flush();
                    return len;
                } else {
                    LogUtil.e(TAG, mSocketName + " Send Data Fail, mOutputStream is null");
                    throw new IOException();
                }
            } catch (Exception e) {
                LogUtil.e(TAG, mSocketName + " IOException, Send Data Fail " + e.getMessage() + " " + Thread.currentThread().getName());
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        public void run() {
            while (isRunning) {
                LogUtil.e(TAG, "Begin to listen in " + mThreadName + " " + mSocketPort);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (null != mServerSocket && isRunning) {
                        mSocket = mServerSocket.accept();
                        if (null == mSocket) {
                            LogUtil.d(TAG, "One client connected fail: " + mThreadName);
                        }
                        LogUtil.e(TAG, "One client connected in " + mThreadName);
                        mSocket.setTcpNoDelay(true);
                        mSocket.setSendBufferSize(SEND_BUFFER_SIZE);
                        mSocket.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);

                        mInputStream = new BufferedInputStream(mSocket.getInputStream());
                        mOutputStream = new BufferedOutputStream(mSocket.getOutputStream());

                        if (mSocketName.equals(CommonParams.SERVER_SOCKET_MIUDRIVE_VR_NAME)) {
//                            CarlifeDeviceInfoManager.getInstance().sendMicRecordWakeUpStart();
//                            CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_VR_MODULE_ID, ModuleStatusModel.VR_STATUS_RUNNING);
                            CarlifeDeviceInfoManager.getInstance().sendMicRecordRecogStart();
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "Get Exception in " + mThreadName);
                    e.printStackTrace();
                    return;
                }
                try {
                    while (mSocket != null && isRunning) {
                        if (!mSocket.isConnected() || mOutputStream == null) {
                            LogUtil.d(TAG, "socket is disconnected when read data");
                            break;
                        }
                        if (mSocketName.equals(CommonParams.SERVER_SOCKET_NAME)
                                || mSocketName.equals(CommonParams.SERVER_SOCKET_TOUCH_NAME)) {
                            if (readData(msg, 0, CommonParams.MSG_CMD_HEAD_SIZE_BYTE) < 0) {
                                continue;
                            }
                            lenMsgHead = CommonParams.MSG_CMD_HEAD_SIZE_BYTE;
                            lenMsgData = (int) ByteConvert.bytesToShort(new byte[]{msg[0], msg[1]});

                            byte[] carlifeMsgByte = new byte[lenMsgHead + lenMsgData];
                            System.arraycopy(msg, 0, carlifeMsgByte, 0, lenMsgHead);
                            if (msg.length < lenMsgData) {
                                msg = new byte[lenMsgData];
                            }
                            if (readData(msg, 0, lenMsgData) < 0) {
                                continue;
                            }
                            System.arraycopy(msg, 0, carlifeMsgByte, lenMsgHead, lenMsgData);
                            CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(false);
                            carlifeMsg.fromByteArray(carlifeMsgByte, carlifeMsgByte.length);
                            DigitalTrans.dumpData(TAG, "RECV CarlifeMsg CMD ", carlifeMsg, false);
                            CarlifeCmdProtocol.carlifeMsgProtocol(carlifeMsg);
                        } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_AUDIO_VR_NAME)) {
                            if (readData(msg, 0, CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE) < 0) {
                                continue;
                            }
                            lenMsgHead = CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE;
                            lenMsgData = ByteConvert.bytesToInt(new byte[]{msg[0], msg[1], msg[2], msg[3]});

                            byte[] carlifeVRMsgByte = new byte[lenMsgHead + lenMsgData];
                            System.arraycopy(msg, 0, carlifeVRMsgByte, 0, lenMsgHead);
                            if (msg.length < lenMsgData) {
                                msg = new byte[lenMsgData];
                            }
                            if (readData(msg, 0, lenMsgData) < 0) {
                                continue;
                            }
                            System.arraycopy(msg, 0, carlifeVRMsgByte, lenMsgHead, lenMsgData);
                            CarlifeVRMessage carlifeVRMessage = new CarlifeVRMessage(false);
                            carlifeVRMessage.fromByteArray(carlifeVRMsgByte, carlifeVRMsgByte.length);
                            DigitalTrans.dumpData(TAG, "RECV CarlifeVRMsg", carlifeVRMessage, true);
                            writeMiuVRMessage(carlifeVRMessage.getData(), carlifeVRMessage.length);
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.write(carlifeVRMessage.getData(), 0, carlifeVRMessage.length);
                                    fileOutputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_MIUDRIVE_NAME)) {
                            if (readData(msg, 0, CommonParams.MIU_MSG_CMD_HEAD_SIZE_BYTE) < 0) {
                                break;
                            }
                            lenMsgHead = CommonParams.MIU_MSG_CMD_HEAD_SIZE_BYTE;
                            lenMsgData = msg[0];
                            byte[] carlifeMiuMsgByte = new byte[lenMsgHead + lenMsgData];
                            System.arraycopy(msg, 0, carlifeMiuMsgByte, 0, lenMsgHead);
                            if (msg.length < lenMsgData) {
                                msg = new byte[lenMsgData];
                            }
                            if (readData(msg, 0, lenMsgData) < 0) {
                                break;
                            }
                            System.arraycopy(msg, 0, carlifeMiuMsgByte, lenMsgHead, lenMsgData);
                            CarlifeMiuCmdMessage carlifeMiuCmdMessage = new CarlifeMiuCmdMessage(false);
                            carlifeMiuCmdMessage.fromByteArray(carlifeMiuMsgByte, carlifeMiuMsgByte.length);
                            DigitalTrans.dumpData(TAG, "RECV CarlifeMiudriveMsg CMD", carlifeMiuCmdMessage, false);
                            carlifeMiudriveMsgProtocol(carlifeMiuCmdMessage);
                        }
                    }
                } catch (Exception ex) {
                    LogUtil.e(TAG, mSocketName + " get Exception in ReadThread " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
}
