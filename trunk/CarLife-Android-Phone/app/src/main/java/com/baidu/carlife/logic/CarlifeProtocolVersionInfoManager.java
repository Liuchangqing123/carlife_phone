
package com.baidu.carlife.logic;

import android.os.Message;
import com.baidu.carlife.connect.ConnectClient;
import com.baidu.carlife.protobuf.CarlifeProtocolVersionMatchStatusProto;
import com.baidu.carlife.protobuf.CarlifeProtocolVersionProto;
import com.baidu.carlife.connect.CarlifeCmdMessage;
import com.baidu.carlife.CommonParams;
import com.baidu.carlife.util.LogUtil;

public class CarlifeProtocolVersionInfoManager {

    public static final String TAG = "CarlifeProtocolInfoManager";

    private static CarlifeProtocolVersionInfoManager mInstance = null;
    private static CarlifeProtocolVersionProto.CarlifeProtocolVersion mMdProtocolVersion = null;
    private static CarlifeProtocolVersionProto.CarlifeProtocolVersion mHuProtocolVersion = null;
    private static CarlifeProtocolVersionMatchStatusProto.CarlifeProtocolVersionMatchStatus mProtocolMatchStatus = null;

    private CarlifeProtocolVersionInfoManager() {

    }

    public static CarlifeProtocolVersionInfoManager getInstance() {
        if (null == mInstance) {
            synchronized (CarlifeProtocolVersionInfoManager.class) {
                if (null == mInstance) {
                    mInstance = new CarlifeProtocolVersionInfoManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        CarlifeProtocolVersionProto.CarlifeProtocolVersion.Builder builder = CarlifeProtocolVersionProto.CarlifeProtocolVersion.newBuilder();
        builder.setMajorVersion(CommonParams.PROTOCOL_VERSION_MAJOR_VERSION);
        builder.setMinorVersion(CommonParams.PROTOCOL_VERSION_MINOR_VERSION);

        mHuProtocolVersion = builder.build();
    }

    public CarlifeProtocolVersionProto.CarlifeProtocolVersion getHuProtocolVersion() {
        return mHuProtocolVersion;
    }

    public void setMdProtocolVersion(CarlifeProtocolVersionProto.CarlifeProtocolVersion info) {
        mMdProtocolVersion = info;
    }

    public CarlifeProtocolVersionProto.CarlifeProtocolVersion getMdProtocolVersion() {
        return mMdProtocolVersion;
    }

    public void setProtocolMatchStatus(CarlifeProtocolVersionMatchStatusProto.CarlifeProtocolVersionMatchStatus info) {
        mProtocolMatchStatus = info;
    }

    public CarlifeProtocolVersionMatchStatusProto.CarlifeProtocolVersionMatchStatus getProtocolMatchStatus() {
        return mProtocolMatchStatus;
    }

    public void sendProtocolMatchStatus() {
        try {
            CarlifeCmdMessage protocolM = new CarlifeCmdMessage(true);
            protocolM.setServiceType(CommonParams.MSG_CMD_PROTOCOL_VERSION_MATCH_STATUS);
            protocolM.setData(mProtocolMatchStatus.toByteArray());
            protocolM.setLength(mProtocolMatchStatus.getSerializedSize());
            Message msgTmp = Message.obtain(null, protocolM.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, protocolM);
            ConnectClient.getInstance().sendMsgToService(msgTmp);
        } catch (Exception ex) {
            LogUtil.e(TAG, "sendProtocolMatchStatus fail " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
