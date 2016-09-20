package com.unimelb.feelinglucky.snapsheet.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by leveyleonhardt on 9/7/16.
 */
public class SharedPreferencesUtils {
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("snapsheet", Context.MODE_PRIVATE);
    }
}
