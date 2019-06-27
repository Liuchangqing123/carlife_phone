package com.baidu.carlife.camera;

import android.app.Activity;
import android.util.Log;
import android.view.Surface;

public class Camera2 {
    static final String TAG = "Camera2";
    android.hardware.Camera mCamera;
    int mCurrentCamIndex = 0;
    boolean previewing;

    public void setCamera(android.hardware.Camera camera) {
        this.mCamera = camera;
    }

    public int getCurrentCamIndex() {
        return this.mCurrentCamIndex;
    }

    public boolean getPreviewing() {
        return this.previewing;
    }

    public android.hardware.Camera initCamera() {
        int cameraCount = 0;
        android.hardware.Camera cam = null;
        android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
        cameraCount = android.hardware.Camera.getNumberOfCameras();
        previewing = true;

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            android.hardware.Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = android.hardware.Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera2 failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }

    public void StopCamera(android.hardware.Camera mCamera) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        previewing = false;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
