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

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import com.didi365.carlife.android.phone.util.CarlifeUtil;
import com.didi365.carlife.android.phone.util.CommonParams;
import com.didi365.carlife.android.phone.util.DigitalTrans;
import com.didi365.carlife.android.phone.util.LogUtil;
import java.util.ArrayList;

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
                        LogUtil.e(TAG, "Send Msg to Socket, what = 0x" + DigitalTrans.algorismToHEXString(msg.what, 8));
                        if (msg.what == CommonParams.MSG_CMD_HU_PROTOCOL_VERSION) {
                            LogUtil.e(TAG, "MSG_CMD_HU_PROTOCOL_VERSION");
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
