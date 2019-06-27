package com.camera;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceHolder {
    Context context;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    SurfaceViewCallback callback = new SurfaceViewCallback();

    public void setCameraSurfaceHolder(Context context, SurfaceView surfaceView) {
        this.context = context;
        this.surfaceView = surfaceView;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(callback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        callback.setContext(context);
    }

    public boolean startCamara() {
        return callback.startCamera();
    }

    public void stopCamara() {
        callback.stopCamera();
    }
}


