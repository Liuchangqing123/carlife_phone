
package com.didi365.carlife.logic;

import android.bluetooth.BluetoothAdapter;
import android.os.Message;
import android.text.TextUtils;

import com.didi365.carlife.connect.ConnectClient;
import com.baidu.carlife.protobuf.CarlifeDeviceInfoProto;
import com.didi365.carlife.connect.CarlifeCmdMessage;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.util.LogUtil;

public class CarlifeDeviceInfoManager {

    public static final String TAG = "CarlifeDeviceInfoManager";

    private static CarlifeDeviceInfoManager mInstance = null;
    private static CarlifeDeviceInfoProto.CarlifeDeviceInfo mDeviceInfo = null;
    private static CarlifeDeviceInfoProto.CarlifeDeviceInfo mPhoneDeviceInfo = null;

    public static byte[] HU_AUTHEN = new byte[]{0x00, 0x2E, (byte) 0xA0, (byte) 0xB2, 0x00, 0x01, (byte) 0x80, 0x48, 0x0A, 0x2C, 0x23, 0x4E, 0x4B, 0x1E, 0x62, 0x47, 0x6C, 0x51, 0x4D, 0x62, 0x62, 0x2A, 0x13, 0x17, 0x15, 0x66, 0x5F, 0x31, 0x2E, 0x32, 0x2E, 0x30, 0x4B, 0x7F, 0x40, 0x2F, 0x5B, 0x41, 0x76, 0x39,
            0x4A, 0x32, 0x30, 0x00, 0x74, 0x77, 0x15, 0x64, 0x5F, 0x31, 0x2E, 0x32, 0x2E, 0x30};

    private byte[] MD_AUTHEN = new byte[]{0x00, (byte) 0xAF, 0x00, 0x00, 0x00, 0x01, 0x00, 0x49, 0x0A, (byte) 0xAC, 0x01, 0x58, 0x6A, 0x6A, 0x46, 0x52, 0x41, 0x54, 0x2F, 0x71, 0x4D, 0x4E, 0x76, 0x4D, 0x53, 0x31, 0x53, 0x77, 0x34, 0x78, 0x35, 0x61, 0x62, 0x7A, 0x49, 0x2B, 0x47, 0x67, 0x47, 0x2F,
            0x6A, 0x33, 0x4B, 0x59, 0x7A, 0x6B, 0x75, 0x4D, 0x4C, 0x64, 0x70, 0x72, 0x78, 0x73, 0x6D, 0x76, 0x36, 0x32, 0x71, 0x7A, 0x74, 0x4E, 0x30, 0x65, 0x78, 0x46, 0x32, 0x70, 0x38, 0x53, 0x4F, 0x76, 0x78, 0x4E, 0x74, 0x39, 0x2B, 0x58, 0x36, 0x77,
            0x6A, 0x72, 0x46, 0x6D, 0x54, 0x6C, 0x67, 0x32, 0x6C, 0x6F, 0x32, 0x53, 0x2F, 0x62, 0x51, 0x75, 0x44, 0x45, 0x75, 0x59, 0x54, 0x46, 0x4B, 0x69, 0x71, 0x72, 0x73, 0x68, 0x4E, 0x4F, 0x56, 0x36, 0x61, 0x53, 0x44, 0x65, 0x6A, 0x56, 0x5A, 0x41,
            0x46, 0x32, 0x43, 0x58, 0x77, 0x67, 0x63, 0x38, 0x31, 0x38, 0x49, 0x48, 0x49, 0x77, 0x67, 0x57, 0x74, 0x6A, 0x42, 0x78, 0x38, 0x59, 0x6A, 0x46, 0x6D, 0x6E, 0x62, 0x71, 0x4A, 0x38, 0x6F, 0x46, 0x63, 0x36, 0x4E, 0x75, 0x4F, 0x76, 0x63, 0x6D,
            0x4D, 0x51, 0x42, 0x6C, 0x34, 0x51, 0x34, 0x2B, 0x6A, 0x68, 0x50, 0x30, 0x62, 0x6D, 0x4F, 0x38, 0x6F, 0x67, 0x44, 0x49, 0x5A, 0x30, 0x3D};

    private CarlifeDeviceInfoManager() {
    }

    public static CarlifeDeviceInfoManager getInstance() {
        if (null == mInstance) {
            synchronized (CarlifeDeviceInfoManager.class) {
                if (null == mInstance) {
                    mInstance = new CarlifeDeviceInfoManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        try {
            CarlifeDeviceInfoProto.CarlifeDeviceInfo.Builder builder = CarlifeDeviceInfoProto.CarlifeDeviceInfo.newBuilder();
            builder.setOs(CommonParams.TYPE_OF_OS);
            builder.setBoard(android.os.Build.BOARD);
            builder.setBootloader(android.os.Build.BOOTLOADER);
            builder.setBrand(android.os.Build.BRAND);
            builder.setCpuAbi(android.os.Build.CPU_ABI);
            builder.setCpuAbi2(android.os.Build.CPU_ABI2);
            builder.setDevice(android.os.Build.DEVICE);
            builder.setDisplay(android.os.Build.DISPLAY);
            builder.setFingerprint(android.os.Build.FINGERPRINT);
            builder.setHardware(android.os.Build.HARDWARE);
            builder.setHost(android.os.Build.HOST);
            builder.setCid(android.os.Build.ID);
            builder.setManufacturer(android.os.Build.MANUFACTURER);
            builder.setModel(android.os.Build.MODEL);
            builder.setProduct(android.os.Build.PRODUCT);
            builder.setSerial(android.os.Build.SERIAL);

            builder.setCodename(android.os.Build.VERSION.CODENAME);
            builder.setIncremental(android.os.Build.VERSION.INCREMENTAL);
            builder.setRelease(android.os.Build.VERSION.RELEASE);
            builder.setSdk(android.os.Build.VERSION.SDK);
            builder.setSdkInt(android.os.Build.VERSION.SDK_INT);

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter != null) {
                String addrString = mBluetoothAdapter.getAddress();
                if (!TextUtils.isEmpty(addrString)) {
                    builder.setBtaddress(addrString);
                } else {
                    builder.setBtaddress("unknow");
                }

            } else {
                builder.setBtaddress("unknow");
            }
            mDeviceInfo = builder.build();
        } catch (Exception ex) {
            LogUtil.e(TAG, "init error");
            ex.printStackTrace();
        }
    }

    public CarlifeDeviceInfoProto.CarlifeDeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    public void setPhoneDeviceInfo(CarlifeDeviceInfoProto.CarlifeDeviceInfo info) {
        mPhoneDeviceInfo = info;
    }

    public CarlifeDeviceInfoProto.CarlifeDeviceInfo getPhoneDeviceInfo() {
        return mPhoneDeviceInfo;
    }

    public void sendCarlifeDeviceInfo() {
        try {
            CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
            carlifeMsg.setServiceType(CommonParams.MSG_CMD_MD_INFO);
            carlifeMsg.setData(mDeviceInfo.toByteArray());
            carlifeMsg.setLength(mDeviceInfo.getSerializedSize());
            Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
            ConnectClient.getInstance().sendMsgToService(msgTmp);
        } catch (Exception ex) {
            LogUtil.e(TAG, "send hu info error");
            ex.printStackTrace();
        }
    }

    public void sendMicRecordWakeUpStart() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MIC_RECORD_WAKEUP_START);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void sendMicRecordRecogStart() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MIC_RECORD_RECOG_START);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void sendMicRecordEnd() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MIC_RECORD_END);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void sendCarlifeForeground() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_FOREGROUND);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void sendCarlifeScreenOn() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_SCREEN_ON);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void responseCarlifeAuthen(CarlifeCmdMessage carlifeMsg) {
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void responseAuthenResult(CarlifeCmdMessage carlifeMsg) {
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void sendAuthenResult() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MD_AUTHEN_RESULT);
        carlifeMsg.setData(new byte[]{0x08, 0x01});
        carlifeMsg.setLength(2);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void requestFeatureConfig() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MD_FEATURE_CONFIG_REQUEST);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }
}
