package com.unimelb.feelinglucky.snapsheet.Thread;

import android.content.Context;

import com.unimelb.feelinglucky.snapsheet.Util.UpdateDeviceIdUtils;

/**
 * Created by leveyleonhardt on 10/12/16.
 */

public class UpdateDeviceIdThread extends Thread {
    private Context context;
    private String deviceId;

    public UpdateDeviceIdThread(Context context, String deviceId) {
        this.context = context;
        this.deviceId = deviceId;
    }

    @Override
    public void run() {
        UpdateDeviceIdUtils.updateDeviceId(context, deviceId);
    }
}
