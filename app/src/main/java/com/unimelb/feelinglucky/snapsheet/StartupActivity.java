package com.unimelb.feelinglucky.snapsheet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.Database.UserDataOpenHelper;
import com.unimelb.feelinglucky.snapsheet.NetworkService.LoginService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Startup.StartupFragment;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.FetchFriendUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 8/27/16.
 */
public class StartupActivity extends AppCompatActivity {
    private static String TAG = "StartupActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        new getDatabaseTask().execute(getApplicationContext());
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(this);
        if (sharedPreferences.contains("username") && sharedPreferences.contains("email") && sharedPreferences.contains("password") && sharedPreferences.contains("birthday")) {
            User user = new User();
            user.setEmail(sharedPreferences.getString("email", null));
            user.setPassword(sharedPreferences.getString("password", null));
            login(this, user);
        } else {
            jumpToLoginFragment();
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
                    DatabaseUtils.refreshUserDb(DatabaseInstance.database, info);
                    FetchFriendUtils.fetchFriends(info.getUsername());
//                    DatabaseUtils.refreshFriendDb(DatabaseInstance.database, info.getFriend());
                    Intent intent = new Intent(context, SnapSheetActivity.class);
                    context.startActivity(intent);
                    finish();
                } else {
                    jumpToLoginFragment();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void jumpToLoginFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_startup_container);
        if (fragment == null) {
            fragment = new StartupFragment();
            fm.beginTransaction().add(R.id.activity_startup_container, fragment).commit();
        }
    }

    private class getDatabaseTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... params) {
            DatabaseInstance.database = new UserDataOpenHelper(params[0]).getWritableDatabase();
            if (DatabaseInstance.database != null)
                Log.i("xxx", "database created");
            return null;
        }
    }
}
