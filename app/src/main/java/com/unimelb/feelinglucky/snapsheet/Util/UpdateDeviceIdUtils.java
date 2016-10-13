package com.unimelb.feelinglucky.snapsheet.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.UpdateDeviceIdService;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 10/12/16.
 */

public class UpdateDeviceIdUtils {
    public static void updateDeviceId(Context context, String deviceId) {
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context);
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
        UpdateDeviceIdService updateDeviceIdService = retrofit.create(UpdateDeviceIdService.class);
        String username = sharedPreferences.getString("username", "");
        Log.i("deviceId_login", username);
        Call call = updateDeviceIdService.updateDeviceId(username, deviceId);
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
    }
}
