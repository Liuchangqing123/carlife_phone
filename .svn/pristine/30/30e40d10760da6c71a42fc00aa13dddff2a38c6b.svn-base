package com.didi365.carlife.android.phone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;

import com.didi365.carlife.android.phone.util.LogUtil;

/**
 * Created by zheng on 2019/3/27
 */
public class UsbDetachedReceiver extends BroadcastReceiver {

    private final String TAG = UsbDetachedReceiver.class.getSimpleName();

    private UsbDetachedListener mUsbDetachedListener;

    public UsbDetachedReceiver(UsbDetachedListener usbDetachedListener) {
        mUsbDetachedListener = usbDetachedListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.v(TAG, "onReceive " + intent);
        if (intent != null && mUsbDetachedListener != null) {
            if (intent.getAction().equals(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)) {
                mUsbDetachedListener.usbAttached();
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_ACCESSORY_DETACHED)) {
                mUsbDetachedListener.usbDetached();
            }
        }
    }

    public interface UsbDetachedListener {

        void usbAttached();

        void usbDetached();

    }
}
