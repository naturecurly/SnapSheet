package com.unimelb.feelinglucky.snapsheet.Util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.unimelb.feelinglucky.snapsheet.R;

/**
 * Created by leveyleonhardt on 9/8/16.
 */
public class StatusBarUtils {
    public static void setStatusBarVisable(Context context) {
        ((AppCompatActivity) (context)).getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                 View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        ((AppCompatActivity) (context)).getWindow().setNavigationBarColor(Color.TRANSPARENT);
        ((AppCompatActivity) (context)).getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.status_bar_color));
    }

    public static void setStatusBarInvisable(Context context) {
        ((AppCompatActivity) (context)).getWindow().getDecorView().setSystemUiVisibility(
// View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        ((AppCompatActivity) (context)).getWindow().setNavigationBarColor(Color.TRANSPARENT);
        ((AppCompatActivity) (context)).getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}
