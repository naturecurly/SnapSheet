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
            sharedPreferences.edit().putString("deviceId", deviceId).commit();
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
            UpdateDeviceIdService updateDeviceIdService = retrofit.create(UpdateDeviceIdService.class);
            Call call = updateDeviceIdService.updateDeviceId(sharedPreferences.getString("username", null), deviceId);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        User user = (User) response.body();
                        DatabaseUtils.refreshUserDb(DatabaseInstance.database, user);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        } else {
            sharedPreferences.edit().putString("deviceId", deviceId).commit();
        }
    }
}
