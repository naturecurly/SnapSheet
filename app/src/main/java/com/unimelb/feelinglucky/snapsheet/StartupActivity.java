package com.unimelb.feelinglucky.snapsheet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.unimelb.feelinglucky.snapsheet.Startup.StartupFragment;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

/**
 * Created by leveyleonhardt on 8/27/16.
 */
public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(this);
        if (sharedPreferences.contains("username") && sharedPreferences.contains("email") && sharedPreferences.contains("password") && sharedPreferences.contains("birthday")) {
            Intent intent = new Intent(this, SnapSheetActivity.class);
            startActivity(intent);
            finish();
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_startup_container);
        if (fragment == null) {
            fragment = new StartupFragment();
            fm.beginTransaction().add(R.id.activity_startup_container, fragment).commit();
        }
    }
}
