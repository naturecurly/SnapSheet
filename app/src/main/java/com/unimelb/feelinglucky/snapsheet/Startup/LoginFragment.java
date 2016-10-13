package com.unimelb.feelinglucky.snapsheet.Startup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.NetworkService.LoginService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.UpdateDeviceIdService;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.Thread.UpdateDeviceIdThread;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.Md5Crypto;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;
import com.unimelb.feelinglucky.snapsheet.Util.UpdateDeviceIdUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 8/28/16.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        usernameEditText = (EditText) view.findViewById(R.id.fragment_login_username_edit_text);
        passwordEditText = (EditText) view.findViewById(R.id.fragment_login_password_edit_text);
        loginButton = (Button) view.findViewById(R.id.fragment_login_button);
        loginButton.setEnabled(false);
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setEmail(usernameEditText.getText().toString());
                user.setPassword(Md5Crypto.encrypt(passwordEditText.getText().toString()));

                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                final LoginService loginService = retrofit.create(LoginService.class);
                Call<User> call = loginService.login(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User loginUser = response.body();
                            SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(getActivity());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", loginUser.getEmail());
                            editor.putString("password", loginUser.getPassword());
                            editor.putString("username", loginUser.getUsername());
                            editor.putLong("birthday", loginUser.getBirthday().getTime());
                            String remoteDeviceId = loginUser.getDevice_id();
                            String localDeviceId = sharedPreferences.getString("deviceId", "");
                            editor.putString("deviceId", localDeviceId);
                            editor.commit();


                            Log.i("deviceId_login", localDeviceId);
//                            UpdateDeviceIdUtils.updateDeviceId(getActivity(), "testid");
//                            String localDeviceId = sharedPreferences.getString("deviceId", "");
                            UpdateDeviceIdUtils.updateDeviceId(getActivity(), localDeviceId);
                            if (DatabaseInstance.database == null) {
                                Log.i("database", "database is null");
                            }
                            DatabaseUtils.refreshUserDb(DatabaseInstance.database, loginUser);
                            DatabaseUtils.refreshFriendDb(DatabaseInstance.database, loginUser.getFriend());
                            Intent intent = new Intent(getActivity(), SnapSheetActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "Log in failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i(TAG, t.getMessage());
                    }
                });
            }
        });
        return view;
    }
}
