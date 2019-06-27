package com.baidu.carlife.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;

/**
 * Created by zheng on 2019/3/27
 */
public class OpenAccessoryReceiver extends BroadcastReceiver {

    private OpenAccessoryListener mOpenAccessoryListener;

    public OpenAccessoryReceiver(OpenAccessoryListener openAccessoryListener) {
        mOpenAccessoryListener = openAccessoryListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        UsbAccessory usbAccessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
        if (mOpenAccessoryListener != null) {
            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                if (usbAccessory != null) {
                    mOpenAccessoryListener.openAccessoryModel(usbAccessory);
                } else {
                    mOpenAccessoryListener.openAccessoryError();
                }
            } else {
                mOpenAccessoryListener.openAccessoryError();
            }
        }
    }

    public interface OpenAccessoryListener {

        void openAccessoryModel(UsbAccessory usbAccessory);

        void openAccessoryError();
    }
}
