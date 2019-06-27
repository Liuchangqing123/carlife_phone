package com.baidu.carlife.connect;

import com.baidu.carlife.CarlifePhoneApplication;
import com.baidu.carlife.CommonParams;
import com.baidu.carlife.encryption.EncryptSetupManager;
import com.baidu.carlife.logic.BtHfpManager;
import com.baidu.carlife.logic.BtHfpProtocolHelper;
import com.baidu.carlife.logic.CarlifeDeviceInfoManager;
import com.baidu.carlife.logic.CarlifeProtocolVersionInfoManager;
import com.baidu.carlife.model.ModuleStatusModel;
import com.baidu.carlife.protobuf.CarlifeBTHfpConnectionProto;
import com.baidu.carlife.protobuf.CarlifeBTStartIdentifyReqProto;
import com.baidu.carlife.protobuf.CarlifeCarGpsProto;
import com.baidu.carlife.protobuf.CarlifeDeviceInfoProto;
import com.baidu.carlife.protobuf.CarlifeFeatureConfigListProto;
import com.baidu.carlife.protobuf.CarlifeFeatureConfigProto;
import com.baidu.carlife.protobuf.CarlifeHuRsaPublicKeyResponseProto;
import com.baidu.carlife.protobuf.CarlifeProtocolVersionMatchStatusProto;
import com.baidu.carlife.protobuf.CarlifeVehicleInfoListProto;
import com.baidu.carlife.protobuf.CarlifeVehicleInfoProto;
import com.baidu.carlife.protobuf.CarlifeVideoEncoderInfoProto;
import com.baidu.carlife.protobuf.CarlifeVideoFrameRateProto;
import com.baidu.carlife.util.ByteConvert;
import com.baidu.carlife.util.CarlifeUtil;
import com.baidu.carlife.util.DecodeUtil;
import com.baidu.carlife.util.LogUtil;
import com.baidu.carlife.vehicle.CarDataManager;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.List;

/**
 * Created by zheng on 2019/5/27
 */
public class CarlifeCmdProtocol {

    private final static String TAG = CarlifeCmdProtocol.class.getSimpleName();

    public static void carlifeMsgProtocol(CarlifeCmdMessage carlifeMsg) {
        switch (carlifeMsg.getServiceType()) {
            case CommonParams.MSG_CMD_HU_PROTOCOL_VERSION:
                try {
                    CarlifeProtocolVersionMatchStatusProto.CarlifeProtocolVersionMatchStatus.Builder builderMatch = CarlifeProtocolVersionMatchStatusProto.CarlifeProtocolVersionMatchStatus.newBuilder();
                    builderMatch.setMatchStatus(CommonParams.PROTOCOL_VERSION_MATCH);
                    CarlifeProtocolVersionMatchStatusProto.CarlifeProtocolVersionMatchStatus mProtocolMatchStatus = builderMatch.build();
                    CarlifeProtocolVersionInfoManager.getInstance().setProtocolMatchStatus(mProtocolMatchStatus);
                    CarlifeProtocolVersionInfoManager.getInstance().sendProtocolMatchStatus();
                    ConnectManager.getInstance().setIsProtocolVersionMatch(true);
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                }
                break;
            case CommonParams.MSG_CMD_STATISTIC_INFO:
                EncryptSetupManager.getInstance().requestPublicKey();
                CarlifeDeviceInfoManager.getInstance().sendCarlifeForeground();
                CarlifeDeviceInfoManager.getInstance().sendCarlifeScreenOn();
                break;
            case CommonParams.MSG_CMD_HU_INFO:
                try {
                    CarlifeDeviceInfoProto.CarlifeDeviceInfo deviceInfo = CarlifeDeviceInfoProto.CarlifeDeviceInfo.parseFrom(carlifeMsg.getData());
                    LogUtil.e(TAG, "deviceInfo " + deviceInfo);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                CarlifeDeviceInfoManager.getInstance().sendCarlifeDeviceInfo();
                CarlifeDeviceInfoManager.getInstance().requestFeatureConfig();
                CarDataManager.getInstance().requestSubcribe();
                break;
            case CommonParams.MSG_CMD_VIDEO_ENCODER_INIT:
                CarlifeVideoEncoderInfoProto.CarlifeVideoEncoderInfo videoInfo = null;
                try {
                    videoInfo = CarlifeVideoEncoderInfoProto.CarlifeVideoEncoderInfo.parseFrom(carlifeMsg.getData());
                    LogUtil.e(TAG, "videoInfo " + videoInfo.getWidth() + " " + videoInfo.getHeight() + " " + videoInfo.getFrameRate());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, e.getMessage());
                }
                if (videoInfo != null) {
                    CarlifePhoneApplication.screenWidth = videoInfo.getWidth();
                    CarlifePhoneApplication.screenHeight = videoInfo.getHeight();
                    CarlifePhoneApplication.frameRate = videoInfo.getFrameRate();
                    CarlifeUtil.sendVideoCodecMsg(videoInfo.getWidth(), videoInfo.getHeight(), videoInfo.getFrameRate());
                } else {
                    CarlifeUtil.sendVideoCodecMsg(1024, 600, 25);
                }
                break;
            case CommonParams.MSG_CMD_MODULE_CONTROL:
                CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_PHONE_MODULE_ID, ModuleStatusModel.PHONE_STATUS_IDLE);
                CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_NAVI_MODULE_ID, ModuleStatusModel.NAVI_STATUS_IDLE);
                CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_MUSIC_MODULE_ID, ModuleStatusModel.MUSIC_STATUS_IDLE);
                CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_VR_MODULE_ID, ModuleStatusModel.VR_STATUS_IDLE);
                CarlifeUtil.sendModuleControlToHu(ModuleStatusModel.CARLIFE_MIC_MODULE_ID, ModuleStatusModel.MIC_STATUS_USE_VEHICLE_MIC);
                break;
            case CommonParams.MSG_CMD_VIDEO_ENCODER_START:
                ConnectClient.getInstance().setIsConnected(true);
                ConnectHeartBeat.getInstance().startConnectHeartBeatTimer();
                DecodeUtil.getInstance().startDecode();
                CarDataManager.getInstance().requestCarVehicle(CarDataManager.MODULE_GPS_DATA, 1, 1);
                break;
            case CommonParams.MSG_CMD_VIDEO_ENCODER_PAUSE:
                DecodeUtil.getInstance().pauseDecode();
                break;
            case CommonParams.MSG_CMD_VIDEO_ENCODER_FRAME_RATE_CHANGE:
                CarlifeVideoFrameRateProto.CarlifeVideoFrameRate videoFrameRate;
                try {
                    videoFrameRate = CarlifeVideoFrameRateProto.CarlifeVideoFrameRate.parseFrom(carlifeMsg.getData());
                    LogUtil.d(TAG, "videoFrameRate==" + videoFrameRate);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case CommonParams.MSG_CMD_HU_RSA_PUBLIC_KEY_RESPONSE:
                CarlifeHuRsaPublicKeyResponseProto.CarlifeHuRsaPublicKeyResponse keyResponse;
                try {
                    keyResponse = CarlifeHuRsaPublicKeyResponseProto.CarlifeHuRsaPublicKeyResponse.parseFrom(carlifeMsg.getData());
                    EncryptSetupManager.getInstance().setRsaPublicKey(keyResponse.getRsaPublicKey());
                    EncryptSetupManager.getInstance().sendAesKey();
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case CommonParams.MSG_CMD_HU_FEATURE_CONFIG_RESPONSE:
                CarlifeFeatureConfigListProto.CarlifeFeatureConfigList featureConfigList;
                try {
                    featureConfigList = CarlifeFeatureConfigListProto.CarlifeFeatureConfigList.parseFrom(carlifeMsg.getData());
                    List<CarlifeFeatureConfigProto.CarlifeFeatureConfig> configList = featureConfigList.getFeatureConfigList();
                    for (CarlifeFeatureConfigProto.CarlifeFeatureConfig config : configList) {
                        LogUtil.d(TAG, "featureConfig==" + config.getKey() + " " + config.getValue());
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case CommonParams.MSG_CMD_CAR_DATA_SUBSCRIBE_RSP:
                CarlifeVehicleInfoListProto.CarlifeVehicleInfoList vehicleInfoList;
                try {
                    vehicleInfoList = CarlifeVehicleInfoListProto.CarlifeVehicleInfoList.parseFrom(carlifeMsg.getData());
                    for (CarlifeVehicleInfoProto.CarlifeVehicleInfo vehicleInfo : vehicleInfoList.getVehicleInfoList()) {
                        LogUtil.d(TAG, "vehicleInfo " + vehicleInfo);
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case CommonParams.MSG_CMD_CAR_GPS:
                CarlifeCarGpsProto.CarlifeCarGps carlifeCarGps;
                try {
                    carlifeCarGps = CarlifeCarGpsProto.CarlifeCarGps.parseFrom(carlifeMsg.getData());
                    LogUtil.d(TAG, "carlifeCarGps " + carlifeCarGps);
                    byte[] carlifeGpsByte = new byte[carlifeMsg.length + CommonParams.MIU_MSG_CMD_HEAD_SIZE];
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MIU_MSG_CMD_TYPE_FIX), 0, carlifeGpsByte, 0, 4);
                    System.arraycopy(ByteConvert.intToBytes(CommonParams.MIU_MSG_CMD_GPS_DATA), 3, carlifeGpsByte, 4, 1);
                    System.arraycopy(carlifeMsg.getData(), 0, carlifeGpsByte, CommonParams.MIU_MSG_CMD_HEAD_SIZE, carlifeMsg.length);
                    carlifeGpsByte[0] = (byte) (carlifeGpsByte.length - 1);
                    AOAConnectManager.getInstance().writeMiuCmdMessage(carlifeGpsByte, carlifeGpsByte.length);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case CommonParams.MSG_CMD_HU_AES_REC_RESPONSE:
                EncryptSetupManager.getInstance().setEncryptSwitch(true);
                break;
            case CommonParams.MSG_CMD_HU_AUTHEN_REQUEST:
                CarlifeDeviceInfoManager.getInstance().responseAuthenToHu();
                break;
            case CommonParams.MSG_CMD_HU_AUTHEN_RESULT:
                CarlifeDeviceInfoManager.getInstance().sendAuthenResult();
                break;
            case CommonParams.MSG_CMD_BT_HFP_CONNECTION:
                CarlifeBTHfpConnectionProto.CarlifeBTHfpConnection btHfpConnection;
                try {
                    btHfpConnection = CarlifeBTHfpConnectionProto.CarlifeBTHfpConnection.parseFrom(carlifeMsg.getData());
                    LogUtil.d(TAG, "btHfpConnection " + btHfpConnection);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case CommonParams.MSG_CMD_BT_START_IDENTIFY_REQ:
                CarlifeBTStartIdentifyReqProto.CarlifeBTStartIdentifyReq btStartIdentifyReq;
                try {
                    btStartIdentifyReq = CarlifeBTStartIdentifyReqProto.CarlifeBTStartIdentifyReq.parseFrom(carlifeMsg.getData());
                    LogUtil.d(TAG, "btStartIdentifyReq " + btStartIdentifyReq);
                    BtHfpProtocolHelper.btStartIdentify(BtHfpManager.BT_HFP_IDENTIFY_SUCCEED, "unknown");
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
