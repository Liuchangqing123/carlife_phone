
package com.didi365.carlife.model;

public class ModuleStatusModel {

    public static final int CARLIFE_PHONE_MODULE_ID = 1;
    public static final int PHONE_STATUS_IDLE = 0;
    public static final int PHONE_STATUS_INCOMING = 1;
    public static final int PHONE_STATUS_OUTGOING = 2;
    

    public static final int CARLIFE_NAVI_MODULE_ID = 2;
    public static final int NAVI_STATUS_IDLE = 0;
    public static final int NAVI_STATUS_START = 1;
    public static final int NAVI_STATUS_STOP = 2;

    public static final int CARLIFE_MUSIC_MODULE_ID = 3;
    public static final int MUSIC_STATUS_IDLE = 0;
    public static final int MUSIC_STATUS_RUNNING = 1;

    public static final int CARLIFE_VR_MODULE_ID = 4;
    public static final int VR_STATUS_IDLE = 0;
    public static final int VR_STATUS_RUNNING = 1;
    public static final int VR_STATUS_MIC_NOT_SUPPORTED = 2;
    public static final int CARLIFE_CONNECT_MODULE_ID = 5;

    public static final int CARLIFE_MIC_MODULE_ID = 6;
    public static final int MIC_STATUS_USE_VEHICLE_MIC = 0;
    public static final int MIC_STATUS_USE_MOBILE_MIC = 1;
    public static final int MIC_STATUS_NOT_SUPPORTED = 2;

    private int moduleId;
    private int statusId;

    public ModuleStatusModel(int module, int status) {
        this.moduleId = module;
        this.statusId = status;
    }

    public int getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(int value) {
        this.moduleId = value;
    }

    public int getStatusId() {
        return this.statusId;
    }

    public void setStatusId(int value) {
        this.statusId = value;
    }
}
