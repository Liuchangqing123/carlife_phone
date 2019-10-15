
package com.didi365.carlife.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.util.LogUtil;

/**
 * Created by zheng on 2019/3/29
 */
public class ConnectServiceReceiver extends BroadcastReceiver {

    private static final String TAG = "ConnectServiceReceiver";

    public static final String CARLIFE_CONNECT_SERVICE_START = "com.didi365.carlife.connect.ConnectServiceStart";
    public static final String CARLIFE_CONNECT_SERVICE_STOP = "com.didi365.carlife.connect.ConnectServiceStop";

    private Context mContext = null;
    private Handler mHandler = null;

    public ConnectServiceReceiver(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CARLIFE_CONNECT_SERVICE_START);
        filter.addAction(CARLIFE_CONNECT_SERVICE_STOP);
        mContext.registerReceiver(this, filter);
    }

    public void unregisterReceiver() {
        // LogUtils.d(TAG, "unregister UsbConnectServiceReceiver");
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == mHandler) {
            LogUtil.e(TAG, "mHandler is null");
            return;
        }

        String action = intent.getAction();
        Message msg = new Message();
        msg.what = CommonParams.MSG_CONNECT_SERVICE_MSG;

        if (action.equals(CARLIFE_CONNECT_SERVICE_START)) {
            LogUtil.d(TAG, "start connect service");
            msg.arg1 = CommonParams.MSG_CONNECT_SERVICE_MSG_START;
        } else if (action.equals(CARLIFE_CONNECT_SERVICE_STOP)) {
            LogUtil.d(TAG, "stop connect service");
            msg.arg1 = CommonParams.MSG_CONNECT_SERVICE_MSG_STOP;
        }
        mHandler.sendMessage(msg);
    }
}
