package com.baidu.carlife.connect;

import com.baidu.carlife.CommonParams;
import com.baidu.carlife.util.ByteConvert;
import com.baidu.carlife.util.DigitalTrans;
import com.baidu.carlife.util.LogUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zheng on 2019/5/27
 */
public class UsbMuxdConnectManager {

    private final String TAG = UsbMuxdConnectManager.class.getSimpleName();

    public static int ConnectType = ConnectManager.CONNECTED_BY_USBMUXD;
    private static final String SOCKET_READ_THREAD_NAME = "SocketReadThread";

    private static UsbMuxdConnectManager mInstance = null;

    private final String UsbMuxdPrefix = "UsbMuxd_";

    private static final int LEN_OF_MSG_HEAD = 8;

    public static final int SOCKET_USBMUXD_PORT = 7240;
    public static final int SOCKET_VIDEO_USBMUXD_PORT = 8240;
    public static final int SOCKET_AUDIO_USBMUXD_PORT = 9240;
    public static final int SOCKET_AUDIO_TTS_USBMUXD_PORT = 9241;
    public static final int SOCKET_AUDIO_VR_USBMUXD_PORT = 9242;
    public static final int SOCKET_TOUCH_USBMUXD_PORT = 9340;

    private SocketReadThread mSocketReadThread = null;
    private SocketReadThread mSocketReadVideoThread = null;
    private SocketReadThread mSocketReadAudioThread = null;
    private SocketReadThread mSocketReadAudioVRThread = null;
    private SocketReadThread mSocketReadTouchThread = null;
    private SocketReadThread mSocketReadAudioTTSThread = null;

    private String audio = "/sdcard/usbtest.pcm";
    private FileOutputStream fileOutputStream;

    private UsbMuxdConnectManager() {
        try {
            fileOutputStream = new FileOutputStream(audio);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static UsbMuxdConnectManager getInstance() {
        if (null == mInstance) {
            synchronized (UsbMuxdConnectManager.class) {
                if (null == mInstance) {
                    mInstance = new UsbMuxdConnectManager();
                }
            }
        }
        return mInstance;
    }

    public void startSocketReadThread() {
        try {
            mSocketReadThread = new SocketReadThread(SOCKET_USBMUXD_PORT, UsbMuxdPrefix + CommonParams.SERVER_SOCKET_NAME);
            mSocketReadThread.start();

            mSocketReadAudioThread = new SocketReadThread(SOCKET_AUDIO_USBMUXD_PORT, UsbMuxdPrefix + CommonParams.SERVER_SOCKET_AUDIO_NAME);
            mSocketReadAudioThread.start();

            mSocketReadAudioVRThread = new SocketReadThread(SOCKET_AUDIO_VR_USBMUXD_PORT, UsbMuxdPrefix + CommonParams.SERVER_SOCKET_AUDIO_VR_NAME);
            mSocketReadAudioVRThread.start();

            mSocketReadVideoThread = new SocketReadThread(SOCKET_VIDEO_USBMUXD_PORT, UsbMuxdPrefix + CommonParams.SERVER_SOCKET_VIDEO_NAME);
            mSocketReadVideoThread.start();

            mSocketReadTouchThread = new SocketReadThread(SOCKET_TOUCH_USBMUXD_PORT, UsbMuxdPrefix + CommonParams.SERVER_SOCKET_TOUCH_NAME);
            mSocketReadTouchThread.start();

            mSocketReadAudioTTSThread = new SocketReadThread(SOCKET_AUDIO_TTS_USBMUXD_PORT, UsbMuxdPrefix + CommonParams.SERVER_SOCKET_AUDIO_TTS_NAME);
            mSocketReadAudioTTSThread.start();
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
            if (null != mSocketReadAudioThread) {
                mSocketReadAudioThread.cancel();
                mSocketReadAudioThread = null;
            }
            if (null != mSocketReadVideoThread) {
                mSocketReadVideoThread.cancel();
                mSocketReadVideoThread = null;
            }
            if (null != mSocketReadAudioVRThread) {
                mSocketReadAudioVRThread.cancel();
                mSocketReadAudioVRThread = null;
            }
            if (null != mSocketReadTouchThread) {
                mSocketReadTouchThread.cancel();
                mSocketReadTouchThread = null;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Stop SocketRead Thread Fail");
            e.printStackTrace();
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
            LogUtil.e(TAG, "[" + tag + "]" + msg);
        } catch (Exception e) {
            LogUtil.e("TAG", "dumpData get Exception");
            e.printStackTrace();
        }
    }

    public int writeCarlifeCmdMessage(CarlifeCmdMessage msg) {
        if (null == mSocketReadThread) {
            LogUtil.e(TAG, "write error: mSocketReadThread is null");
            return -1;
        }
        return mSocketReadThread.writeData(msg);
    }

    public int writeVideoData(byte[] data, int len) {
        if (null == mSocketReadVideoThread) {
            LogUtil.e(TAG, "write error: mSocketReadVideoThread is null");
            return -1;
        }
        return mSocketReadVideoThread.writeData(data, 0, len);
    }

    public int writeAudioData(byte[] data, int len) {
        if (null == mSocketReadAudioThread) {
            LogUtil.e(TAG, "write error: mSocketReadAudioThread is null");
            return -1;
        }
        return mSocketReadAudioThread.writeData(data, 0, len);
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

        public SocketReadThread(int port, String name) {
            try {
                mThreadName = name + SOCKET_READ_THREAD_NAME;
                setName(mThreadName);
                mSocketPort = port;
                mSocketName = name;
                LogUtil.e(TAG, "Create " + mThreadName + " " + mSocketPort);

                mServerSocket = new ServerSocket(mSocketPort);
                isRunning = true;
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
                LogUtil.e(TAG, mSocketName + " IOException, Receive Data Fail " + e.getMessage());
                e.printStackTrace();
                return r;
            }
        }

        public int writeData(CarlifeCmdMessage msg) {
            try {
                if (null != mOutputStream) {
                    dumpData("SEND CarlifeMsg CMD", msg);
                    mOutputStream.write(msg.toByteArray());
                    mOutputStream.flush();
                    if (msg.getLength() > 0) {
                        mOutputStream.write(msg.getData());
                        mOutputStream.flush();
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
                LogUtil.e(TAG, mSocketName + " IOException, Send Data Fail " + e.getMessage());
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        public void run() {
            LogUtil.e(TAG, "Begin to listen in " + mThreadName + " " + mSocketPort);
            try {
                if (null != mServerSocket && isRunning) {
                    mSocket = mServerSocket.accept();
                    if (null == mSocket) {
                        LogUtil.d(TAG, "One client connected fail: " + mThreadName);
                    }
                    LogUtil.e(TAG, "One client connected in " + mThreadName);

                    mInputStream = new BufferedInputStream(mSocket.getInputStream());
                    mOutputStream = new BufferedOutputStream(mSocket.getOutputStream());

                    if (mSocketName.equals(UsbMuxdPrefix + CommonParams.SERVER_SOCKET_NAME)) {
                        ConnectManager.getInstance().initConnectType(ConnectManager.CONNECTED_BY_USBMUXD);
                        ConnectClient.getInstance().setIsConnecting(true);
                        ConnectManager.getInstance().startAllConnectSocket();
                    }
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "Get Exception in " + mThreadName);
                e.printStackTrace();
                return;
            }
            try {
                while (mSocket != null && isRunning) {
                    if (!mSocket.isConnected()) {
                        LogUtil.e(TAG, "socket is disconnected when read data");
                        break;
                    }
                    if (mSocketName.equals(UsbMuxdPrefix + CommonParams.SERVER_SOCKET_NAME)) {
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
                        dumpData("RECV CarlifeMsg CMD ", carlifeMsg);
                        CarlifeCmdProtocol.carlifeMsgProtocol(carlifeMsg);
                    } else if (mSocketName.equals(UsbMuxdPrefix + CommonParams.SERVER_SOCKET_TOUCH_NAME)) {
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
                        CarlifeCmdMessage carlifeTouchMsg = new CarlifeCmdMessage(false);
                        carlifeTouchMsg.fromByteArray(carlifeMsgByte, carlifeMsgByte.length);
                        dumpData("RECV CarlifeTouchMessage", carlifeTouchMsg);
                        ConnectManager.getInstance().writeCarlifeTouchMessage(carlifeTouchMsg);
                    } else if (mSocketName.equals(UsbMuxdPrefix + CommonParams.SERVER_SOCKET_AUDIO_VR_NAME)) {
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
                        dumpData("RECV CarlifeVRMsg CMD", carlifeVRMessage);
                        AOAConnectManager.getInstance().writeMiuVRMessage(carlifeVRMessage.getData(), carlifeVRMessage.length);

                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.write(carlifeVRMessage.getData(), 0, carlifeVRMessage.length);
                                fileOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                LogUtil.e(TAG, mSocketName + " get Exception in ReadThread " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
