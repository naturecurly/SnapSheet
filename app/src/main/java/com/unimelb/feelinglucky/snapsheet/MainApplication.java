package com.unimelb.feelinglucky.snapsheet;

import android.app.Application;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;

/**
 * Created by leveyleonhardt on 10/11/16.
 */

public class MainApplication extends Application implements IAdobeAuthClientCredentials {
    private static final String CREATIVE_SDK_CLIENT_ID = "d4ebe5031b10428aa1f644bda1780468";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "3100418e-eb0e-4855-992e-b9abe6dace57";


    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());

    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }
}
