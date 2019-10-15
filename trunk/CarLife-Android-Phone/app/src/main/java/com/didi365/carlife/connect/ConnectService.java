
package com.didi365.carlife.connect;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import com.didi365.carlife.logic.CarlifeDeviceInfoManager;
import com.didi365.carlife.logic.CarlifeProtocolVersionInfoManager;
import com.didi365.carlife.util.CarlifeUtil;
import com.didi365.carlife.util.LogUtil;
import java.util.LinkedList;
import java.util.List;

public class ConnectService extends Service {

    public static final int MSG_SEND_DISCARD = -1;

    private static final String TAG = "ConnectService";
    private static final int SERVICE_CACHED_MSG_LIMIT = 100;
    private static final String CONNECT_SERVICE_HANDLER_NAME = "ConnectServiceHandler";

    private ConnectServiceProxy mConnectServiceProxy = null;
    private Handler mConnectServiceProxyHandler = null;
    private Handler mConnectServiceHandler;

    private Messenger mMessenger;
    private List<Message> mCachedMessage = new LinkedList<Message>();

    private ConnectManager mConnectManager = null;

    private class ConnectServiceHandler extends Handler {

        public ConnectServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mConnectServiceProxyHandler != null) {
                mConnectServiceProxyHandler.handleMessage(msg);

                if (mCachedMessage.size() > 0) {
                    Message cachedMsg = mCachedMessage.remove(0);
                    mConnectServiceHandler.sendMessage(cachedMsg);
                }
            } else {
                if (mCachedMessage.size() >= SERVICE_CACHED_MSG_LIMIT) {
                    Message oldMsg = mCachedMessage.remove(0);
                    Message replayMsg = Message.obtain(null, MSG_SEND_DISCARD, oldMsg);
                    try {
                        LogUtil.e(TAG, "Send MSG_SEND_DISCARD, oldMsg what = " + Integer.toString(oldMsg.what));
                        oldMsg.replyTo.send(replayMsg);
                    } catch (Throwable t) {
                        LogUtil.e(TAG, "Send MSG_SEND_DISCARD Error");
                        t.printStackTrace();
                    }
                }
                Message tmp = Message.obtain(msg);
                mCachedMessage.add(tmp);

                createConnectService();
            }
        }
    }

    private void createConnectService() {
        LogUtil.d(TAG, "createConnectService");
        try {
            if (mConnectServiceProxy == null || mConnectServiceProxyHandler == null) {
                mConnectServiceProxy = new ConnectServiceProxy(this);
                mConnectServiceProxyHandler = mConnectServiceProxy.getHandler();
            }
            if (mCachedMessage.size() > 0) {
                Message cachedMsg = mCachedMessage.remove(0);
                mConnectServiceHandler.sendMessage(cachedMsg);
            }

            mConnectManager = ConnectManager.getInstance();
            mConnectManager.startConnectThread("createConnectService");

            HandlerThread handlerThread = new HandlerThread(CONNECT_SERVICE_HANDLER_NAME);
            handlerThread.start();
            mConnectServiceHandler = new ConnectServiceHandler(CarlifeUtil.getLooper(handlerThread));
            mMessenger = new Messenger(mConnectServiceHandler);
        } catch (Throwable t) {
            mConnectServiceProxy = null;
            mConnectServiceProxyHandler = null;
            t.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG, "ConnectService onBind()");
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d(TAG, "ConnectService onUnbind()");
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        LogUtil.d(TAG, "ConnectService onRebind()");
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "ConnectService onCreate()");
        super.onCreate();
        AOAConnectManager.getInstance().init(this);
        AOAConnectManager.getInstance().startSocketReadThread();
        CarlifeUtil.getInstance().init(this);
        CarlifeDeviceInfoManager.getInstance().init();
        CarlifeProtocolVersionInfoManager.getInstance().init();
        ConnectManager.getInstance().init(this);
        createConnectService();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        LogUtil.d(TAG, "ConnectService onStart(), startId = " + startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "ConnectService onDestroy()");
        super.onDestroy();
    }
}
