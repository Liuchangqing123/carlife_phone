/******************************************************************************
 * Copyright 2017 The Baidu Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package com.didi365.carlife.android.phone.connect;

import com.didi365.carlife.android.phone.encryption.EncryptSetupManager;
import com.didi365.carlife.android.phone.util.ByteConvert;
import com.didi365.carlife.android.phone.util.CommonParams;
import com.didi365.carlife.android.phone.util.LogUtil;

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
