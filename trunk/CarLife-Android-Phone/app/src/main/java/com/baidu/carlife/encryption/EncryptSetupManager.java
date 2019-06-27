
package com.baidu.carlife.encryption;

import android.os.Message;
import com.baidu.carlife.connect.ConnectClient;
import com.baidu.carlife.protobuf.CarlifeMdAesKeyRequestProto;
import com.baidu.carlife.connect.CarlifeCmdMessage;
import com.baidu.carlife.CommonParams;

public class EncryptSetupManager {
    private Boolean mIsEnableEncryption = false;
    private static EncryptSetupManager mInstance;
    private RSAManager mRSAManager = new RSAManager();
    private String mAESKey = "1234567890123456";

    public static EncryptSetupManager getInstance() {
        if (mInstance == null) {
            mInstance = new EncryptSetupManager();
        }
        return mInstance;
    }

    public void requestPublicKey() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MD_RSA_PUBLIC_KEY_REQUEST);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void sendAesKey() {
        CarlifeMdAesKeyRequestProto.CarlifeMdAesKeyRequest.Builder builder = CarlifeMdAesKeyRequestProto.CarlifeMdAesKeyRequest.newBuilder();
        builder.setAesKey(mRSAManager.encrypt(getAesKey(), mRSAManager.getPublicKey()));
        CarlifeMdAesKeyRequestProto.CarlifeMdAesKeyRequest aesKeyRequest = builder.build();
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MD_AES_KEY_SEND_REQUEST);
        carlifeMsg.setData(aesKeyRequest.toByteArray());
        carlifeMsg.setLength(aesKeyRequest.getSerializedSize());
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public boolean isEncryptEnable() {
        if (!EncryptConfig.DEBUG_ENABLE) {
            return false;
        }
        if (EncryptConfig.AES_ENCRYPT_AS_BEGINE) {
            return true;
        }
        return mIsEnableEncryption;
    }

    public void setEncryptSwitch(boolean isEnable) {
        mIsEnableEncryption = isEnable;
    }

    public String getAesKey() {
        return mAESKey;
    }

    public void setRsaPublicKey(String key) {
        mRSAManager.publicKeyGenerate(key);
    }

    public void onDisConnection() {
        setEncryptSwitch(false);
    }

    private EncryptSetupManager() {
    }
}
