
package com.didi365.carlife.android.phone.connect;

import com.didi365.carlife.android.phone.encryption.AESManager;
import com.didi365.carlife.android.phone.encryption.EncryptSetupManager;
import com.didi365.carlife.android.phone.util.ByteConvert;
import com.didi365.carlife.android.phone.util.CommonParams;
import com.didi365.carlife.android.phone.util.LogUtil;

public class CarlifeCmdMessage {

    private static final String TAG = "CarlifeCmdMessage";
    protected static int total_index = 0;

    protected int index = CommonParams.MSG_CMD_DEFAULT_VALUE;

    protected AESManager mReadAESManager = new AESManager();

    /*
     * length       0~15    16  The length of the data.
     * reserved     16~31   16  Keep the field
     * service type 32~63   32  The service type of the message.
     * data         64~n    0~m The stream of bytes after PB serialization
     */
    protected int length = CommonParams.MSG_CMD_DEFAULT_VALUE;
    protected int reserved = CommonParams.MSG_CMD_TYPE_RESERVED;
    protected int serviceType = CommonParams.MSG_CMD_DEFAULT_VALUE;

    protected byte[] data = null;

    public CarlifeCmdMessage(boolean isSend) {
        if (isSend) {
            index = ++total_index;
        }
    }

    public boolean fromByteArray(byte[] msg) {
        if (msg.length != CommonParams.MSG_CMD_HEAD_SIZE_BYTE) {
            LogUtil.e(TAG, "fromByteArray fail: length not equal");
            return false;
        }
        int tmpParam = 0;
        byte tmpByte = 0;

        try {
            tmpParam = (int) ByteConvert.bytesToShort(new byte[]{msg[0], msg[1]});
            setLength(tmpParam);

            tmpParam = (int) ByteConvert.bytesToShort(new byte[]{msg[2], msg[3]});
            setReserved(tmpParam);

            tmpParam = (int) ByteConvert.bytesToInt(new byte[]{msg[4], msg[5], msg[6], msg[7]});
            setServiceType(tmpParam);
        } catch (Exception e) {
            LogUtil.e(TAG, "fromByteArray fail: get exception");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean fromByteArray(byte[] msg, int len) {
        if (len < CommonParams.MSG_CMD_HEAD_SIZE_BYTE) {
            LogUtil.e(TAG, "fromByteArray fail: length not equal");
            return false;
        }
        int tmpParam = 0;
        byte tmpByte = 0;

        try {
            tmpParam = (int) ByteConvert.bytesToShort(new byte[]{msg[0], msg[1]});
            setLength(tmpParam);

            tmpParam = (int) ByteConvert.bytesToShort(new byte[]{msg[2], msg[3]});
            setReserved(tmpParam);

            tmpParam = (int) ByteConvert.bytesToInt(new byte[]{msg[4], msg[5], msg[6], msg[7]});
            setServiceType(tmpParam);

            byte[] data = new byte[len - CommonParams.MSG_CMD_HEAD_SIZE_BYTE];
            System.arraycopy(msg, CommonParams.MSG_CMD_HEAD_SIZE_BYTE, data, 0, len - CommonParams.MSG_CMD_HEAD_SIZE_BYTE);
            decryptData(data, data.length);
        } catch (Exception e) {
            LogUtil.e(TAG, "fromByteArray fail: get exception");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[CommonParams.MSG_CMD_HEAD_SIZE_BYTE];
        byte[] tmpBytes = null;
        int tmpParam = 0;
        int i = 0;
        try {
            tmpBytes = ByteConvert.intToBytes(length);
            bytes[i++] = tmpBytes[2];
            bytes[i++] = tmpBytes[3];

            tmpBytes = ByteConvert.intToBytes(reserved);
            bytes[i++] = tmpBytes[2];
            bytes[i++] = tmpBytes[3];

            tmpBytes = ByteConvert.intToBytes(serviceType);
            bytes[i++] = tmpBytes[0];
            bytes[i++] = tmpBytes[1];
            bytes[i++] = tmpBytes[2];
            bytes[i++] = tmpBytes[3];
        } catch (Exception e) {
            LogUtil.e(TAG, "toByteArray fail: get exception");
            e.printStackTrace();
            return null;
        }

        return bytes;
    }

    public byte[] messageToByteArray() {
        byte[] bytes = new byte[CommonParams.MSG_CMD_HEAD_SIZE_BYTE];
        byte[] tmpBytes = null;
        int tmpParam = 0;
        int i = 0;
        try {
            tmpBytes = ByteConvert.intToBytes(length);
            bytes[i++] = tmpBytes[2];
            bytes[i++] = tmpBytes[3];

            tmpBytes = ByteConvert.intToBytes(reserved);
            bytes[i++] = tmpBytes[2];
            bytes[i++] = tmpBytes[3];

            tmpBytes = ByteConvert.intToBytes(serviceType);
            bytes[i++] = tmpBytes[0];
            bytes[i++] = tmpBytes[1];
            bytes[i++] = tmpBytes[2];
            bytes[i++] = tmpBytes[3];

            byte[] msgBuffer = new byte[CommonParams.MSG_CMD_HEAD_SIZE_BYTE + data.length];
            System.arraycopy(bytes, 0, msgBuffer, 0, bytes.length);
            System.arraycopy(data, 0, msgBuffer, CommonParams.MSG_CMD_HEAD_SIZE_BYTE, data.length);
            return msgBuffer;
        } catch (Exception e) {
            LogUtil.e(TAG, "toByteArray fail: get exception");
            e.printStackTrace();
            return null;
        }
    }

    public void decryptData(byte[] data, int size) {
        if (EncryptSetupManager.getInstance().isEncryptEnable()) {
            byte[] decryptByte = mReadAESManager.decrypt(data, size);
            if (decryptByte != null) {
                LogUtil.d(TAG, "decrypt success!!");
                setData(decryptByte);
                setLength(decryptByte.length);
            } else {
                LogUtil.d(TAG, "decrypt failed!!");
            }
        } else {
            setData(data);
        }
    }

    public void setIndex(int ind) {
        if (ind < 0) {
            LogUtil.e(TAG, "set index fail: %d", ind);
            return;
        }
        index = ind;
    }

    public int getIndex() {
        return index;
    }

    public void setReserved(int ty) {
        if (ty < 0) {
            LogUtil.e(TAG, "set reserved fail: %d", ty);
            return;
        }
        reserved = ty;
    }

    public int getReserved() {
        return reserved;
    }

    public void setServiceType(int ty) {
        if (ty < 0) {
            LogUtil.e(TAG, "set service type fail: %d", ty);
            return;
        }
        serviceType = ty;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setLength(int len) {
        if (len < 0 || len > CommonParams.MSG_CMD_MAX_DATA_LEN) {
            LogUtil.e(TAG, "set data len fail: %d", len);
            return;
        }
        length = len;
    }

    public int getLength() {
        return length;
    }

    public void setData(byte[] obj) {
        data = obj;
    }

    public byte[] getData() {
        return data;
    }

}
