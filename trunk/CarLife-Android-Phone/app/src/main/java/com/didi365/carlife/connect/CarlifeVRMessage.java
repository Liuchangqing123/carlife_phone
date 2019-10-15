
package com.didi365.carlife.connect;

import com.didi365.carlife.util.ByteConvert;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.util.LogUtil;

/**
 * Created by zheng on 2019/4/19
 */
public class CarlifeVRMessage extends CarlifeCmdMessage {

    private static final String TAG = "CarlifeCmdMessage";

    public CarlifeVRMessage(boolean isSend) {
        super(isSend);
    }

    public boolean fromByteArray(byte[] msg, int len) {
        if (len < CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE) {
            LogUtil.e(TAG, "fromByteArray fail: length not equal");
            return false;
        }
        int tmpParam = 0;
        byte tmpByte = 0;

        try {
            tmpParam = ByteConvert.bytesToInt(new byte[]{msg[0], msg[1], msg[2], msg[3]});
            setLength(tmpParam);

            tmpParam = ByteConvert.bytesToInt(new byte[]{msg[4], msg[5], msg[6], msg[7]});
            setReserved(tmpParam);

            tmpParam = ByteConvert.bytesToInt(new byte[]{msg[8], msg[9], msg[10], msg[11]});
            setServiceType(tmpParam);

            byte[] data = new byte[len - CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE];
            System.arraycopy(msg, CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE, data, 0, len - CommonParams.MSG_VIDEO_HEAD_SIZE_BYTE);
            decryptData(data, data.length);
        } catch (Exception e) {
            LogUtil.e(TAG, "fromByteArray fail: get exception");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
