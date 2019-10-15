package com.didi365.carlife.logic;

import android.os.Message;
import android.text.TextUtils;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.connect.CarlifeCmdMessage;
import com.didi365.carlife.connect.ConnectClient;
import com.baidu.carlife.protobuf.CarlifeBTIdentifyResultIndProto.CarlifeBTIdentifyResultInd;

/**
 * Created by zheng on 2019/5/28
 */
public class BtHfpProtocolHelper {
    private static final String TAG = BtHfpProtocolHelper.class.getSimpleName();

    public static void btStartIdentify(int status, String address) {
        CarlifeBTIdentifyResultInd btIdentifyResultInd = buildBtStartIdentifyResponse(status, address);
        if (btIdentifyResultInd != null) {
            btStartIdentifyResponseToHU(btIdentifyResultInd);
        }
    }

    private static void btStartIdentifyResponseToHU(CarlifeBTIdentifyResultInd btIdentifyResultInd) {
        CarlifeCmdMessage carlifeMsg = new CarlifeCmdMessage(true);
        carlifeMsg.setServiceType(CommonParams.MSG_CMD_BT_IDENTIFY_RESULT_IND);
        carlifeMsg.setData(btIdentifyResultInd.toByteArray());
        carlifeMsg.setLength(btIdentifyResultInd.getSerializedSize());
        Message msgTmp = Message.obtain(null, carlifeMsg.getServiceType(), CommonParams.MSG_CMD_PROTOCOL_VERSION, 0, carlifeMsg);
        ConnectClient.getInstance().sendMsgToService(msgTmp);
    }

    private static CarlifeBTIdentifyResultInd buildBtStartIdentifyResponse(int status, String address) {
        CarlifeBTIdentifyResultInd.Builder builder = CarlifeBTIdentifyResultInd.newBuilder();
        if (builder == null) {
            return null;
        }
        if (!TextUtils.isEmpty(address)) {
            builder.setAddress(address);
        } else {
            builder.setAddress("unknown");
        }
        builder.setStatus(status);
        return builder.build();
    }
}
