package com.baidu.carlife.connect;

import com.baidu.carlife.util.ByteConvert;
import com.baidu.carlife.CommonParams;
import com.baidu.carlife.util.LogUtil;

/**
 * Created by zheng on 2019/5/15
 */
public class CarlifeMiuCmdMessage extends CarlifeCmdMessage {

    private final String TAG = CarlifeMiuCmdMessage.class.getSimpleName();

    public CarlifeMiuCmdMessage(boolean isSend) {
        super(isSend);
    }

    public boolean fromByteArray(byte[] msg, int len) {
        if (msg.length < CommonParams.MIU_MSG_CMD_HEAD_SIZE_BYTE) {
            LogUtil.e(TAG, "fromByteArray fail: length not equal");
            return false;
        }
        int tmpParam = 0;
        byte tmpByte = 0;

        try {
            tmpParam = (int) ByteConvert.bytesToShort(new byte[]{0x00, msg[0]});
            setLength(tmpParam);

            tmpParam = ByteConvert.bytesToInt(new byte[]{0x00, msg[1], msg[2], msg[3]});
            setReserved(tmpParam);

            tmpParam = (int) ByteConvert.bytesToShort(new byte[]{0x00, msg[4]});
            setServiceType(tmpParam);

            byte[] data = new byte[len - CommonParams.MIU_MSG_CMD_HEAD_SIZE];
            System.arraycopy(msg, CommonParams.MIU_MSG_CMD_HEAD_SIZE, data, 0, len - CommonParams.MIU_MSG_CMD_HEAD_SIZE);
            setData(data);
        } catch (Exception e) {
            LogUtil.e(TAG, "fromByteArray fail: get exception");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
