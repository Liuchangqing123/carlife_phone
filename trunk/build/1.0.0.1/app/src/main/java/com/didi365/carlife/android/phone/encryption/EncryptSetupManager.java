
package com.didi365.carlife.android.phone.encryption;

import com.baidu.carlife.protobuf.CarlifeMdAesKeyRequestProto;
import com.didi365.carlife.android.phone.connect.AOAConnectManager;
import com.didi365.carlife.android.phone.connect.CarlifeCmdMessage;
import com.didi365.carlife.android.phone.util.CommonParams;

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
        AOAConnectManager.getInstance().writeCarlifeCmdMessage(carlifeMsg);
    }

    public void sendAesKey() {
        CarlifeMdAesKeyRequestProto.CarlifeMdAesKeyRequest.Builder builder = CarlifeMdAesKeyRequestProto.CarlifeMdAesKeyRequest.newBuilder();
        builder.setAesKey(mRSAManager.encrypt(getAesKey(), mRSAManager.getPublicKey()));
        CarlifeMdAesKeyRequestProto.CarlifeMdAesKeyRequest aesKeyRequest = builder.build();
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_MD_AES_KEY_SEND_REQUEST);
        carlifeMsg.setData(aesKeyRequest.toByteArray());
        carlifeMsg.setLength(aesKeyRequest.getSerializedSize());
        AOAConnectManager.getInstance().writeCarlifeCmdMessage(carlifeMsg);
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
