package com.baidu.carlife.util;

import android.os.Parcel;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by zheng on 2019/4/11
 */
public class TouchUtil {

    public static byte[] eventToSerializable(InputEvent event) {
        Parcel parcel = Parcel.obtain();

        if (event instanceof MotionEvent) {
            MotionEvent mEvent = (MotionEvent) event;

            int pCount = mEvent.getPointerCount();
            int bCount = pCount * 8 + 12;

//            parcel.setDataSize(bCount);


//            parcel.writeInt32(1);    //type touch
//            parcel.writeInt32(action); //action
//            parcel.writeInt32(1);    //ptCount
//            parcel.writeInt64(0);
//            parcel.writeFloat(1.0);

            parcel.writeInt(1);
            parcel.writeInt(mEvent.getAction());
            parcel.writeInt(1);
            parcel.writeLong(0);
//            parcel.writeLong(mEvent.getEventTimeNano());
            parcel.writeFloat(1.0f);

            parcel.writeFloat(1.0f);
            parcel.writeFloat(1.0f);
            parcel.writeFloat(1.0f);
            parcel.writeFloat(1.0f);
            parcel.writeFloat(1.0f);
            parcel.writeFloat(1.0f);
            parcel.writeFloat(((MotionEvent) event).getX());
            parcel.writeFloat(((MotionEvent) event).getY());

//            for (int i = 0; i < pCount; i++) {
//                MotionEvent.PointerCoords pcoord = new MotionEvent.PointerCoords();
//                mEvent.getPointerCoords(i, pcoord);
//
//                parcel.writeFloat(pcoord.orientation);
//                parcel.writeFloat(pcoord.pressure);
//                parcel.writeFloat(pcoord.size);
//                parcel.writeFloat(pcoord.toolMajor);
//                parcel.writeFloat(pcoord.toolMinor);
//                parcel.writeFloat(pcoord.touchMajor);
//                parcel.writeFloat(pcoord.touchMinor);
//                parcel.writeFloat(((MotionEvent) event).getX());
//                parcel.writeFloat(((MotionEvent) event).getY());
//            }
        }

        if (event instanceof KeyEvent) {
            KeyEvent kEvent = (KeyEvent) event;

            parcel.setDataSize(12);
            parcel.writeInt(2);
            parcel.writeInt(kEvent.getKeyCode());
            parcel.writeInt(kEvent.getAction());
        }

        parcel.setDataPosition(0);
        return parcel.marshall();
    }
}
