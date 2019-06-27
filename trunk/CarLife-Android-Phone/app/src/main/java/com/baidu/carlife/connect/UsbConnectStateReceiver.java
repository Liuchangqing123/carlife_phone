
package com.baidu.carlife.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.baidu.carlife.CommonParams;
import com.baidu.carlife.util.LogUtil;

public class UsbConnectStateReceiver extends BroadcastReceiver {

    private static final String TAG = "UsbConnectStateReceiver";

    private static final String USB_STATE_ACTION = "android.hardware.usb.action.USB_STATE";

    private Context mContext = null;
    private Handler mHandler = null;

    public UsbConnectStateReceiver(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(USB_STATE_ACTION);
        filter.addDataScheme("file");
        mContext.registerReceiver(this, filter);
    }

    public void unregisterReceiver() {
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
        msg.what = CommonParams.MSG_USB_STATE_MSG;

        if (action.equals(USB_STATE_ACTION)) {
            LogUtil.d(TAG, "usb connect is changed");
            if (isUsbConnected(intent)) {
                msg.arg1 = CommonParams.MSG_USB_STATE_MSG_ON;
            } else {
                msg.arg1 = CommonParams.MSG_USB_STATE_MSG_OFF;
            }
        }

        mHandler.sendMessage(msg);
    }

    private boolean isUsbConnected(Intent intent) {
        return intent.getExtras() != null && intent.getExtras().getBoolean("connected");
    }
}
