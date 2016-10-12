package com.unimelb.feelinglucky.snapsheet.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.StartupActivity;

import static com.unimelb.feelinglucky.snapsheet.Database.UserDataOpenHelper.DATABASE_NAME;

/**
 * Created by leveyleonhardt on 10/13/16.
 */

public class LogoutUtils {
    public static void logout(Context context) {
        //clear sharedpreference
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context);
        sharedPreferences.edit().clear().commit();
        //clear database
        DatabaseInstance.database.execSQL("drop table if exists " + DATABASE_NAME);
        Intent intent = new Intent(context, StartupActivity.class);
        context.startActivity(intent);
        ((SnapSheetActivity) context).finish();
    }
}
