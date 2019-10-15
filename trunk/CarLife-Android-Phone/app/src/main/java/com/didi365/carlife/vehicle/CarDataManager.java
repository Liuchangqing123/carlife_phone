package com.didi365.carlife.vehicle;

import android.os.Message;
import com.didi365.carlife.connect.ConnectClient;
import com.baidu.carlife.protobuf.CarlifeVehicleInfoProto;
import com.didi365.carlife.connect.CarlifeCmdMessage;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.util.LogUtil;

/**
 * Created by zheng on 2019/5/17
 */
public class CarDataManager {

    private final String TAG = CarDataManager.class.getSimpleName();
    public static final int MODULE_GPS_DATA = 0;
    public static final int MODULE_CAR_VELOCITY = 1;
    private static CarDataManager mInstance = null;

    private CarDataManager() {
        LogUtil.d(TAG, "Construct CarDataManager");
    }

    public static CarDataManager getInstance() {
        if (mInstance == null) {
            synchronized (CarDataManager.class) {
                if (mInstance == null) {
                    mInstance = new CarDataManager();
                }
            }
        }
        return mInstance;
    }

    public void requestSubcribe() {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_CAR_DATA_SUBSCRIBE_REQ);
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    public void requestCarVehicle(int module, int option, int freq) {
        CarlifeVehicleInfoProto.CarlifeVehicleInfo.Builder builder = CarlifeVehicleInfoProto.CarlifeVehicleInfo.newBuilder();
        builder.setModuleID(module);
        builder.setFlag(option);
        builder.setFrequency(freq);
        CarlifeVehicleInfoProto.CarlifeVehicleInfo vehicleInfo = builder.build();
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_CAR_DATA_START_REQ);
        carlifeMsg.setLength(vehicleInfo.getSerializedSize());
        carlifeMsg.setData(vehicleInfo.toByteArray());
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }
}
