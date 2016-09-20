package com.unimelb.feelinglucky.snapsheet.Startup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.NetworkService.CheckUsernameService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 8/30/16.
 */
public class UsernameFragment extends Fragment {
    private EditText usernameText;
    private TextView usernameHint;
    private Button continueButton;
    private final String TAG = this.getClass().getSimpleName();
    private ColorStateList defaultColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_username, container, false);
        usernameText = (EditText) view.findViewById(R.id.username_edit_text);
        usernameHint = (TextView) view.findViewById(R.id.username_hint);
        defaultColor = usernameHint.getTextColors();
        continueButton = (Button) view.findViewById(R.id.fragment_username_continue_button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usernameText.getText().length() == 0) {
                    usernameHint.setText(R.string.username_null);
                    usernameHint.setTextColor(Color.RED);

                } else {
                    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                    CheckUsernameService checkUsernameService = retrofit.create(CheckUsernameService.class);
                    Call call = checkUsernameService.checkUsername(usernameText.getText().toString());
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {

                                ReturnMessage message = (ReturnMessage) response.body();
                                Log.i(TAG, message.getMessage());

                                if (message.isSuccess() == true) {
                                    usernameHint.setText(R.string.username_exists);
                                    usernameHint.setTextColor(Color.RED);
                                } else {
                                    usernameHint.setText(R.string.signup_username_hint);
                                    usernameHint.setTextColor(defaultColor);

                                    SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", usernameText.getText().toString()).commit();
                                    getFragmentManager().beginTransaction().replace(R.id.activity_startup_container, new BirthdayFragment()).addToBackStack("username").commit();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });
                }
            }
        });
        return view;
    }
}
