package com.unimelb.feelinglucky.snapsheet.Startup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.Dialog.DatePickerFragment;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.RegisterService;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 8/28/16.
 */
public class BirthdayFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "BirthdayFragment";
    private Button signupButton;
    private EditText birthdayText;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birthday, container, false);
        signupButton = (Button) view.findViewById(R.id.fragment_birthday_continue_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                String a = sharedPreferences.getString("email", "email");
                String b = sharedPreferences.getString("password", "password");
                Log.i("TTT", a + " " + b);
                signup();
            }
        });
        birthdayText = (EditText) view.findViewById(R.id.birthday_edit_text);
        birthdayText.setFocusable(false);
        birthdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        return view;
    }


    private void showDateDialog() {
        DialogFragment dialogFragment = DatePickerFragment.newInstance(this);
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    private void signup() {
        User newUser = new User();
        newUser.setEmail(sharedPreferences.getString("email", ""));
        newUser.setPassword(sharedPreferences.getString("password", ""));
        newUser.setBirthday(new Date(sharedPreferences.getLong("birthday", 0)));

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
        RegisterService registerService = retrofit.create(RegisterService.class);
        Call<User> call = registerService.register(newUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i(TAG, "success");
                Intent intent = new Intent(getActivity(), SnapSheetActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("birthday", calendar.getTimeInMillis());
        editor.commit();
        birthdayText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }
}
