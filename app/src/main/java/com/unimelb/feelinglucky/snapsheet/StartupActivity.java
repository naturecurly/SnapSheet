package com.unimelb.feelinglucky.snapsheet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.Database.UserDataOpenHelper;
import com.unimelb.feelinglucky.snapsheet.NetworkService.LoginService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.Startup.StartupFragment;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 8/27/16.
 */
public class StartupActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private static String TAG = "StartupActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        mDatabase = new UserDataOpenHelper(this).getWritableDatabase();

        SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(this);
        if (sharedPreferences.contains("username") && sharedPreferences.contains("email") && sharedPreferences.contains("password") && sharedPreferences.contains("birthday")) {
            User user = new User();
            user.setEmail(sharedPreferences.getString("email", null));
            user.setPassword(sharedPreferences.getString("password", null));
            login(this, user);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.activity_startup_container);
            if (fragment == null) {
                fragment = new StartupFragment();
                fm.beginTransaction().add(R.id.activity_startup_container, fragment).commit();
            }
        }

    }

    public void login(final Context context, User user) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
        LoginService loginService = retrofit.create(LoginService.class);
        Call call = loginService.login(user);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    User info = (User) response.body();

                    Log.i(TAG, info.getFriend()[0]);
                    DatabaseUtils.refreshUserDb(mDatabase, info);
                    DatabaseUtils.refreshFriendDb(mDatabase, info.getFriend());
                    Intent intent = new Intent(context, SnapSheetActivity.class);
                    context.startActivity(intent);
                    finish();
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.activity_startup_container);
                    if (fragment == null) {
                        fragment = new StartupFragment();
                        fm.beginTransaction().add(R.id.activity_startup_container, fragment).commit();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
