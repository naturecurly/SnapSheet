package com.unimelb.feelinglucky.snapsheet.Service;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.UpdateDeviceIdService;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;
import com.unimelb.feelinglucky.snapsheet.Util.UpdateDeviceIdUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 10/7/16.
 */

public class SnapInstanceIdService extends FirebaseInstanceIdService {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String deviceId = FirebaseInstanceId.getInstance().getToken();
        Log.i("deviceId", deviceId);
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(getApplicationContext());

        if (sharedPreferences.contains("username")) {
            Log.i("deviceId", "username existed");
            sharedPreferences.edit().putString("deviceId", deviceId).commit();
            UpdateDeviceIdUtils.updateDeviceId(getApplicationContext(), deviceId);
        } else {
            sharedPreferences.edit().putString("deviceId", deviceId).commit();
        }
    }
}
