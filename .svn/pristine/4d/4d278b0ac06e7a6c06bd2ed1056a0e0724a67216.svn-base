
package com.baidu.carlife.encryption;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;
import com.baidu.carlife.message.MsgBaseHandler;
import com.baidu.carlife.message.MsgHandlerCenter;

public class DebugLogUtil {
    private static final boolean LOG_SWITCH = false;
    private static DebugLogUtil mInstance;
    private static final int MSG_ID_DEBUG = 2222222;
    private static final String MSG_BUNDLE_KEY = "key";
    private Context mContext;
    private DebugLogHandler mDebugLogHandler = new DebugLogHandler();

    private DebugLogUtil() {
        MsgHandlerCenter.registerMessageHandler(mDebugLogHandler);
    }

    public static DebugLogUtil getInstance() {
        if (mInstance == null) {
            mInstance = new DebugLogUtil();
        }

        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
    }

    public void println(String str) {
        if (!LOG_SWITCH) {
            return;
        }

        Message msg = new Message();
        msg.what = MSG_ID_DEBUG;
        Bundle bundle = new Bundle();
        bundle.putString(MSG_BUNDLE_KEY, str);
        msg.setData(bundle);

        mDebugLogHandler.sendMessage(msg);
    }

    private class DebugLogHandler extends MsgBaseHandler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ID_DEBUG:
                    Toast.makeText(mContext, msg.getData().getString(MSG_BUNDLE_KEY), Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void careAbout() {
            addMsg(MSG_ID_DEBUG);
        }
    }
}
