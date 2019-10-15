
package com.didi365.carlife.message;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class MsgBaseHandler extends Handler {

    /**
     * interested messages
     */
    private ArrayList<Integer> mInterests = new ArrayList<Integer>();

    /**
     * subclass should add interested messages in this method.
     */
    public abstract void careAbout();

    public MsgBaseHandler(Looper looper) {
        super(looper);
        careAbout();
    }

    public MsgBaseHandler() {
        super();
        careAbout();
    }

    /**
     * add msgId to interest list
     * @param msgID {@link android.os.Message#what}
     */
    public void addMsg(int msgID) {
        for (Integer i : mInterests) {
            if (i == msgID) {
                return;
            }
        }

        mInterests.add(msgID);
    }

    /**
     * remove msgId from interest list
     * @param msgID {@link android.os.Message#what}
     */
    public void removeMsg(int msgID) {
        for (Iterator<Integer> it = mInterests.iterator(); it.hasNext();) {
            Integer i = it.next();
            if (i == msgID) {
                it.remove();
            }
        }
    }

    /**
     * whether msgId is added to interest list
     * @param msgID {@link android.os.Message#what}
     * @return true for added, false for not
     */
    public boolean isAdded(int msgID) {
        if (mInterests == null) {
            return false;
        }
        for (int i = 0; i < mInterests.size(); i++) {
            if (mInterests.get(i) == msgID) {
                return true;
            }
        }

        return false;
    }

}
