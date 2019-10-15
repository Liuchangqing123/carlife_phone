
package com.didi365.carlife.connect;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import com.didi365.carlife.CommonParams;
import com.didi365.carlife.util.CarlifeUtil;
import com.didi365.carlife.util.LogUtil;
import java.util.ArrayList;

/**
 * Created by zheng on 2019/3/29
 */
public class ConnectServiceProxy {

    private static final String TAG = "ConnectServiceProxy";
    private static final String CONNECT_SERVICE_PROXY_HANDLER_NAME = "ConnectServiceProxyHandler";

    private ArrayList<Messenger> mClients = new ArrayList<Messenger>();

    private Context mContext;
    private Handler mHandler;

    private class ConnectServiceProxyHandler extends Handler {
        public ConnectServiceProxyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == msg) {
                LogUtil.e(TAG, "handleMessage error: msg is null");
                return;
            }
            switch (msg.what) {
                case CommonParams.MSG_REC_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case CommonParams.MSG_REC_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                default:
                    if (msg.arg1 == CommonParams.MSG_CMD_PROTOCOL_VERSION) {
                        LogUtil.d(TAG, "Send Msg to Socket, name = " + CommonParams.getMsgName(msg.what));
                        if (msg.what == CommonParams.MSG_CMD_PROTOCOL_VERSION_MATCH_STATUS
                                || ConnectManager.getInstance().getIsProtocolVersionMatch()) {
                            if (ConnectManager.getInstance().getConnectType() == ConnectManager.CONNECTED_BY_AOA) {
                                AOAConnectManager.getInstance().writeCarlifeCmdMessage((CarlifeCmdMessage) (msg.obj));
                            } else if (ConnectManager.getInstance().getConnectType() == ConnectManager.CONNECTED_BY_ANDROID_DEBUG) {
                                AndroidDebugConnect.getInstance().writeCarlifeCmdMessage((CarlifeCmdMessage) (msg.obj));
                            }
                        }
                    }
                    super.handleMessage(msg);
            }
        }
    }

    public ConnectServiceProxy(Context context) {
        mContext = context;
        HandlerThread handlerThread = new HandlerThread(CONNECT_SERVICE_PROXY_HANDLER_NAME);
        handlerThread.start();
        mHandler = new ConnectServiceProxyHandler(CarlifeUtil.getLooper(handlerThread));
    }

    public Handler getHandler() {
        return mHandler;
    }
}
