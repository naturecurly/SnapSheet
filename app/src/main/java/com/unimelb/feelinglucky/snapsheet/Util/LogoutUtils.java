package com.unimelb.feelinglucky.snapsheet.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.StartupActivity;

import java.io.IOException;

import static com.unimelb.feelinglucky.snapsheet.Database.UserDataOpenHelper.DATABASE_NAME;

/**
 * Created by leveyleonhardt on 10/13/16.
 */

public class LogoutUtils {
    public static void logout(Context context) {
        //clear sharedpreference
        UpdateDeviceIdUtils.updateDeviceId(context, "");
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context);
        sharedPreferences.edit().clear().commit();
        //clear database
        sharedPreferences.edit().putString("deviceId", FirebaseInstanceId.getInstance().getToken()).commit();
        context.deleteDatabase(DATABASE_NAME);
        DatabaseInstance.database = null;
        Intent intent = new Intent(context, StartupActivity.class);
        context.startActivity(intent);
        ((SnapSheetActivity) context).finish();
    }
}
