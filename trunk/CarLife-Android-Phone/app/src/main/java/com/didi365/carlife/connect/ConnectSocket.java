
package com.didi365.carlife.connect;

import com.baidu.carlife.protobuf.CarlifeAuthenResponseProto;
import com.baidu.carlife.protobuf.CarlifeVideoEncoderInfoProto;
import com.didi365.carlife.logic.CarlifeDeviceInfoManager;
import com.didi365.carlife.util.ByteConvert;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.util.DigitalTrans;
import com.didi365.carlife.util.LogUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by zheng on 2019/3/29
 */
public class ConnectSocket {

    private static final String TAG = "ConnectSocket";

    private static final String READ_THREAD_NAME = "ReadThread";
    private static final String WRITE_THREAD_NAME = "WriteThread";
    private static final String CONNECT_THREAD_NAME = "ConnectThread";

    private static final int MAX_BUFFER_BYTES = 4096;
    private static final int SEND_BUFFER_SIZE = 32 * 1024;
    private static final int RECEIVE_BUFFER_SIZE = 32 * 1024;
    private static final String BYTES_FORMAT_TYPE = "utf-8";

    private String connectSocketName = "ConnectSocket";

    private ReadThread mReadThread = null;

    private Socket mSocket = null;
    private BufferedInputStream mInputStream = null;
    private BufferedOutputStream mOutputStream = null;
    private boolean isConnected = false;

    public static final int SLEEP_TIME_MS = 100;

    private static final int LEN_OF_MSG_HEAD = 8;

    private int lenMsgHead = -1;
    private int lenMsgData = -1;

    private byte[] head = new byte[LEN_OF_MSG_HEAD];
    private byte[] msg = new byte[CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE];

    public ConnectSocket(String mSocketName, Socket socket) {
        connectSocketName = mSocketName;
        mSocket = socket;

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
        } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_ANDROID_DEBUG_NAME)) {
            System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID), 0, head, 0, 4);
        } else if (mSocketName.equals(CommonParams.SERVER_SOCKET_ANDROID_DEBUG_VIDEO)) {
            System.arraycopy(ByteConvert.intToBytes(CommonParams.MSG_CHANNEL_ID_VIDEO), 0, head, 0, 4);
        }
    }

    public String getConnectSocketName() {
        return connectSocketName;
    }

    public void startConmunication() {
        LogUtil.d(TAG, "Start Conmunication " + getConnectSocketName());
        if (!isConnected) {
            try {
                mInputStream = new BufferedInputStream(mSocket.getInputStream());
                mOutputStream = new BufferedOutputStream(mSocket.getOutputStream());

                doShakeHands();
                afterShakeHands();
                isConnected = true;

            } catch (Exception e) {
                LogUtil.e(TAG, "Start Conmunication Fail " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void stopConnunication() {
        LogUtil.d(TAG, "Stop Conmunication" + getConnectSocketName());
        if (isConnected) {
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
                isConnected = false;
            } catch (Exception e) {
                LogUtil.e(TAG, "Stop Conmunication Fail");
            }
        }
    }

    private void doShakeHands() {
        LogUtil.d(TAG, "ConnectSocket do shake hands");
        ConnectManager.getInstance().addConnectSocket(this);
    }

    private void afterShakeHands() {
        LogUtil.d(TAG, "ConnectSocket after shake hands");
        if (connectSocketName.equals(CommonParams.SERVER_SOCKET_NAME)
                || connectSocketName.equals(CommonParams.SERVER_SOCKET_ANDROID_DEBUG_NAME)
                || connectSocketName.equals(CommonParams.SERVER_SOCKET_ANDROID_DEBUG_VIDEO)) {
            mReadThread = new ReadThread();
            mReadThread.start();
        }
    }

    public int writeData(byte[] buffer, int len) {
        try {
            if (null != mOutputStream) {
                mOutputStream.write(buffer, 0, len);
                mOutputStream.flush();
                return len;
            } else {
                LogUtil.e(TAG, connectSocketName + " Send Data Fail, mOutputStream is null");
                throw new IOException();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, connectSocketName + " IOException, Send Data Fail");
            e.printStackTrace();
            return -1;
        }
    }

    private CarlifeCmdMessage readData() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(false);
        try {
            if (null != mInputStream) {
                int cnt;
                int r;
                cnt = CommonParams.MSG_CMD_HEAD_SIZE_BYTE;
                byte[] headBuf = new byte[CommonParams.MSG_CMD_HEAD_SIZE_BYTE];
                int headLen = 0;
                while (cnt > 0) {
                    r = mInputStream.read(headBuf, headLen, cnt);
                    if (r > 0) {
                        cnt -= r;
                        headLen += r;
                    } else {
                        LogUtil.e(TAG, connectSocketName + " Receive Carlife Msg Head Error: ret = " + r);
                        throw new IOException();
                    }
                }
                if (headLen == CommonParams.MSG_CMD_HEAD_SIZE_BYTE) {
                    carlifeMsg.fromByteArray(headBuf);
                } else {
                    LogUtil.e(TAG, connectSocketName + " Receive Carlife Msg Head Error: headLen = " + headLen);
                    throw new IOException();
                }
                int len = carlifeMsg.getLength();
                cnt = len;
                byte[] dataBuf = new byte[len];
                int dataLen = 0;
                while (cnt > 0) {
                    r = mInputStream.read(dataBuf, dataLen, cnt);
                    if (r > 0) {
                        cnt -= r;
                        dataLen += r;
                    } else {
                        LogUtil.e(TAG, connectSocketName + " Receive Carlife Msg Data Error: ret = " + r);
                        throw new IOException();
                    }
                }
                if (dataLen == len) {
                    carlifeMsg.setData(dataBuf);
                } else {
                    LogUtil.e(TAG, connectSocketName + " Receive Carlife Msg Data Error: dataLen = " + dataLen);
                    throw new IOException();
                }
            } else {
                LogUtil.e(TAG, connectSocketName + " Receive Data Fail, mInputStream is null");
                throw new IOException();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, connectSocketName + " IOException, Receive Data Fail");
            e.printStackTrace();
            return null;
        }
        return carlifeMsg;
    }

    public int readData(byte[] buffer, int len) {
        int r = -1;
        try {
            if (null != mInputStream) {
                int cnt;

                cnt = len;
                int dataLen = 0;
                while (cnt > 0) {
                    r = mInputStream.read(buffer, dataLen, cnt);
                    if (r > 0) {
                        cnt -= r;
                        dataLen += r;
                    } else {
                        LogUtil.e(TAG, connectSocketName + " Receive Data Error: ret = " + r);
                        throw new IOException();
                    }
                }
                if (dataLen == len) {
                    //LogUtil.v(TAG, connectSocketName + " dataLen == len");
                } else {
                    LogUtil.e(TAG, connectSocketName + " Receive Data Error: dataLen = " + dataLen + " " + len);
                    throw new IOException();
                }
                return dataLen;
            } else {
                LogUtil.e(TAG, connectSocketName + " Receive Data Fail, mInputStream is null");
                throw new IOException();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, connectSocketName + " IOException, Receive Data Fail " + e.getMessage());
            e.printStackTrace();
            return r;
        }
    }

    public int readData(byte[] buffer, int off, int len) {
        int r = -1;
        try {
            if (null != mInputStream) {
                int cnt;

                cnt = len;
                int dataLen = off;
                while (cnt > 0) {
                    r = mInputStream.read(buffer, dataLen, cnt);
                    if (r > 0) {
                        cnt -= r;
                        dataLen += r;
                    } else {
                        LogUtil.e(TAG, connectSocketName + " Receive Data Error: ret = " + r);
                        throw new IOException();
                    }
                }
                if ((dataLen - off) == len) {
                    //LogUtil.v(TAG, connectSocketName + " dataLen == len");
                } else {
                    LogUtil.e(TAG, connectSocketName + " Receive Data Error: dataLen = " + dataLen + " " + len);
                    throw new IOException();
                }
                return dataLen;
            } else {
                LogUtil.e(TAG, connectSocketName + " Receive Data Fail, mInputStream is null");
                throw new IOException();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, connectSocketName + " IOException, Receive Data Fail " + e.getMessage());
            e.printStackTrace();
            return r;
        }
    }

    public BufferedInputStream getInputStream() {
        return mInputStream;
    }

    public BufferedOutputStream getOutputStream() {
        return mOutputStream;
    }

    private class ReadThread extends Thread {
        public ReadThread() {
            setName(READ_THREAD_NAME);
        }

        @Override
        public void run() {
            try {
                sleep(SLEEP_TIME_MS);
                while (isConnected) {
                    if (!mSocket.isConnected()) {
                        LogUtil.e(TAG, "socket is disconnected when read data");
                        break;
                    }
                    if (connectSocketName.equals(CommonParams.SERVER_SOCKET_NAME)) {
                        CarlifeCmdMessage carlifeMsg = readData();
                        if (null != carlifeMsg) {
                            DigitalTrans.dumpData(TAG, "SEND AOA CarlifeMsg", carlifeMsg, false);

                            byte[] carlifeMsgByte = carlifeMsg.messageToByteArray();
                            lenMsgHead = CommonParams.MSG_CMD_HEAD_SIZE_BYTE;
                            lenMsgData = (int) ByteConvert.bytesToShort(new byte[]{carlifeMsgByte[0], carlifeMsgByte[1]}) + lenMsgHead;
                            System.arraycopy(ByteConvert.intToBytes(lenMsgData), 0, head, 4, 4);
                            AOAAccessorySetup.getInstance().bulkTransferOut(head, lenMsgHead, carlifeMsgByte, carlifeMsgByte.length);
                        } else {
                            LogUtil.e(TAG, "read carlife msg fail");
                            break;
                        }
                    } else if (connectSocketName.equals(CommonParams.SERVER_SOCKET_ANDROID_DEBUG_NAME)) {
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
                        DigitalTrans.dumpData(TAG, "AndroidDebug CarlifeMsg SEND", carlifeMsg, false);

                        lenMsgHead = CommonParams.MSG_CMD_HEAD_SIZE_BYTE;
                        lenMsgData = (int) ByteConvert.bytesToShort(new byte[]{carlifeMsgByte[0], carlifeMsgByte[1]}) + lenMsgHead;
                        System.arraycopy(ByteConvert.intToBytes(lenMsgData), 0, head, 4, 4);
//                        AOAAccessorySetup.getInstance().bulkTransferOut(head, lenMsgHead, carlifeMsgByte, carlifeMsgByte.length);

                        if (carlifeMsg.getServiceType() == CommonParams.MSG_CMD_MD_AUTHEN_RESPONSE) {
                            CarlifeAuthenResponseProto.CarlifeAuthenResponse carlifeAuthenResponse = CarlifeAuthenResponseProto.CarlifeAuthenResponse.parseFrom(carlifeMsg.getData());
                            LogUtil.d(TAG, "CarlifeAuthenResponse " + carlifeAuthenResponse.getEncryptValue());
                            CarlifeDeviceInfoManager.getInstance().responseCarlifeAuthen(carlifeMsg);
                        } else if (carlifeMsg.getServiceType() == CommonParams.MSG_CMD_MD_AUTHEN_RESULT) {
                            CarlifeDeviceInfoManager.getInstance().responseAuthenResult(carlifeMsg);
                        } else if (carlifeMsg.getServiceType() == CommonParams.MSG_CMD_VIDEO_ENCODER_INIT_DONE) {
                            CarlifeVideoEncoderInfoProto.CarlifeVideoEncoderInfo videoEncoderInfo = CarlifeVideoEncoderInfoProto.CarlifeVideoEncoderInfo.parseFrom(carlifeMsg.getData());
                            LogUtil.d(TAG, "AndroidDebug CarlifeVideoEncoderInfo " + videoEncoderInfo);
                            stopConnunication();
//                            CarlifePhoneApplication.screenWidth = videoEncoderInfo.getWidth();
//                            CarlifePhoneApplication.screenHeight = videoEncoderInfo.getHeight();
//                            CarlifePhoneApplication.frameRate = videoEncoderInfo.getFrameRate();
//                            CarlifeUtil.sendVideoCodecMsg(videoEncoderInfo.getWidth(), videoEncoderInfo.getHeight(), videoEncoderInfo.getFrameRate());
                        }
                    } else if (connectSocketName.equals(CommonParams.SERVER_SOCKET_ANDROID_DEBUG_VIDEO)) {
                        if (readData(msg, 0, CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE) < 0) {
                            continue;
                        }
                        lenMsgHead = CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE;
                        lenMsgData = ByteConvert.bytesToInt(new byte[]{msg[0], msg[1], msg[2], msg[3]});
                        System.arraycopy(ByteConvert.intToBytes(lenMsgHead + lenMsgData), 0, head, 4, 4);
                        byte[] inputData = new byte[lenMsgHead + lenMsgData];
                        if (readData(inputData, lenMsgHead, lenMsgData) < 0) {
                            continue;
                        }
                        System.arraycopy(msg, 0, inputData, 0, lenMsgHead);
                        AOAAccessorySetup.getInstance().bulkTransferOut(head, LEN_OF_MSG_HEAD, inputData, inputData.length);
                    }
                }
            } catch (InterruptedException e) {
                LogUtil.e(TAG, "get InterruptedException in ReadThread");
                e.printStackTrace();
            } catch (Exception ex) {
                LogUtil.e(TAG, "get Exception in ReadThread");
                ex.printStackTrace();
            }
        }
    }
}
