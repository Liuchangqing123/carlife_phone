
package com.didi365.carlife.message;

import android.os.Message;
import com.didi365.carlife.CommonParams;
import java.util.ArrayList;
import java.util.List;

public class MsgHandlerCenter {

    /**
     * handlers for messages to be delivered
     */
    private static final List<MsgBaseHandler> mHandlerList = new ArrayList<MsgBaseHandler>();

    public MsgHandlerCenter() {
    }

    /**
     * register handler to be an observer
     *
     * @param handler the {@link android.os.Handler} to be registered
     */
    public static void registerMessageHandler(MsgBaseHandler handler) {
        if (null == handler || mHandlerList.contains(handler)) {
            return;
        }

        mHandlerList.add(handler);
    }

    /**
     * unregister handler
     *
     * @param handler the {@link android.os.Handler} to be unregistered
     */
    public static void unRegisterMessageHandler(MsgBaseHandler handler) {
        if (null == handler || (!mHandlerList.contains(handler))) {
            return;
        }
        mHandlerList.remove(handler);
    }

    /**
     * unregister all handler
     *
     */
    public static void unRegisterAllMessageHandler() {
        mHandlerList.clear();
    }

    /**
     * deliver message to all registered handlers
     * @param what {@link Message#what}
     * @param arg1 {@link Message#arg1}
     * @param arg2 {@link Message#arg2}
     * @param b {@link Message#obj}
     * @param delay after delay time to dispatch the message, in milliseconds
     */
    public static void dispatchMessageDelay(int what, int arg1, int arg2, Object b, int delay) {
        if (mHandlerList != null && !mHandlerList.isEmpty()) {
            for (int i = 0; i < mHandlerList.size(); i++) {
                MsgBaseHandler h = mHandlerList.get(i);
                if (h != null && h.isAdded(what)) {
                    Message msg = Message.obtain(h, what, arg1, arg2, b);
                    h.sendMessageDelayed(msg, delay);
                }
            }
        }
    }

    public static void dispatchMessageDelay(int what, int delay) {
        dispatchMessageDelay(what, CommonParams.MSG_ARG_INVALID, CommonParams.MSG_ARG_INVALID,
                null, delay);
    }

    public static void dispatchMessage(int what, int arg1, int arg2, Object b) {
        dispatchMessageDelay(what, arg1, arg2, b, 0);
    }

    public static void dispatchMessage(int what) {
        dispatchMessage(what, CommonParams.MSG_ARG_INVALID, CommonParams.MSG_ARG_INVALID, null);
    }

    public static void dispatchMessage(int what, Object b) {
        dispatchMessage(what, CommonParams.MSG_ARG_INVALID, CommonParams.MSG_ARG_INVALID, b);
    }

    /**
     * remove all messages unhandled
     * @param what {@link Message#what}
     */
    public static void removeMessages(int what) {
        if (mHandlerList != null && !mHandlerList.isEmpty()) {
            for (int i = 0; i < mHandlerList.size(); i++) {
                MsgBaseHandler h = mHandlerList.get(i);
                if (h != null && h.isAdded(what)) {
                    h.removeMessages(what);
                }
            }
        }
    }
}
