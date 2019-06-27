package com.baidu.carlife.logic;

import android.content.Context;

/**
 * Created by zheng on 2019/5/28
 */
public class BtHfpManager {
    private static final String TAG = BtHfpManager.class.getSimpleName();
    private static final String ACTION_BT_CARLIEF_CONN_STATE = "com.baidu.carlife.connection";
    private static final String EXTRA_BT_CARLIFE_CONN_STATE = "com.baidu.carlife.connection.state";
    private static final String ACTION_BT_HFT_CLIENT = "com.baidu.carlifevehicle.bluetoothHfpClient";

    public static final int BT_HFP_NEW_CALL = 1;
    public static final int BT_HFP_OUT_CALL = 2;
    public static final int BT_HFP_CALL_ACTIVE = 3;
    public static final int BT_HFP_NO_CALL_ACTIVE = 4;

    public static final int BT_HFP_CONNECTED = 2;
    public static final int BT_HFP_CONNECTING = 1;
    public static final int BT_HFP_DISCONNECTED = 0;

    public static final int BT_HFP_IDENTIFY_SUCCEED = 1;
    public static final int BT_HFP_IDENTIFY_FAILED = 0;

    public static final int BT_HFP_START_CALL = 1;
    public static final int BT_HFP_TERMINATE_CALL = 2;
    public static final int BT_HFP_ANSWER_CALL = 3;
    public static final int BT_HFP_REJECT_CALL = 4;
    public static final int BT_HFP_DTMF_CODE = 5;
    public static final int BT_HFP_MUTE_MIC = 6;
    public static final int BT_HFP_UNMUTE_MIC = 7;

    public static final int BT_HFP_TYPE_MIC_STATUS = 1;

    public static final int HU_MIC_MUTE = 1;
    public static final int HU_MIC_UNMUTE = 0;

    public static final int BT_HFP_STATUS_INVALID_PARAM = -1;
    public static final int BT_HFP_STATUS_FAILURE = 0;
    public static final int BT_HFP_STATUS_SUCCESS = 1;

    private Context mContext = null;
    private static BtHfpManager mInstance = null;
    public boolean isServiceRunning = false;
}
