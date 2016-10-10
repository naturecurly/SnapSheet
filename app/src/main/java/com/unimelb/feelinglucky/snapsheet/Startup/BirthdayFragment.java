package com.unimelb.feelinglucky.snapsheet.Startup;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.Database.UserDataOpenHelper;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema;
import com.unimelb.feelinglucky.snapsheet.Database.UserDbSchema.UserTable;
import com.unimelb.feelinglucky.snapsheet.Dialog.AlertDialogFragment;
import com.unimelb.feelinglucky.snapsheet.Dialog.DatePickerFragment;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.RegisterService;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.Util.CalculateAge;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import org.w3c.dom.Text;

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
    private TextView birthdayHint;
    private SharedPreferences sharedPreferences;
    private ColorStateList defaultColor;
    private SQLiteDatabase mDatabase;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = SharedPreferencesUtils.getSharedPreferences(getActivity());
        mDatabase = new UserDataOpenHelper(getActivity()).getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birthday, container, false);
        birthdayHint = (TextView) view.findViewById(R.id.signup_birthday_hint);
        defaultColor = birthdayHint.getTextColors();
        signupButton = (Button) view.findViewById(R.id.fragment_birthday_continue_button);
        signupButton.setVisibility(View.GONE);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
//                String a = sharedPreferences.getString("email", "email");
//                String b = sharedPreferences.getString("password", "password");
//                Log.i("TTT", a + " " + b);

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
        if (sharedPreferences.contains("deviceId")) {
            User newUser = new User();
            newUser.setEmail(sharedPreferences.getString("email", ""));
            newUser.setPassword(sharedPreferences.getString("password", ""));
            newUser.setBirthday(new Date(sharedPreferences.getLong("birthday", 0)));
            newUser.setUsername(sharedPreferences.getString("username", ""));
            newUser.setDevice_id(sharedPreferences.getString("deviceId", ""));
            newUser.setMobile(sharedPreferences.getString("mobile", ""));

            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
            RegisterService registerService = retrofit.create(RegisterService.class);
            Call<User> call = registerService.register(newUser);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()) {
                        Log.i(TAG, "success");
                        User user = response.body();
                        Log.i(TAG, user.getUsername());
                        DatabaseUtils.refreshUserDb(mDatabase, user);
                        Intent intent = new Intent(getActivity(), SnapSheetActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    } else {
                        sharedPreferences.edit().clear().commit();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i(TAG, t.getMessage());
                    sharedPreferences.edit().clear().commit();

                }
            });
        } else {
            new AlertDialogFragment().show(getFragmentManager(), "cannot get deviceId");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        if (CalculateAge.isOver18(calendar)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("birthday", calendar.getTimeInMillis());
            editor.commit();
            birthdayText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            birthdayHint.setTextColor(defaultColor);
            signupButton.setVisibility(View.VISIBLE);

        } else {
            birthdayHint.setTextColor(Color.RED);
            signupButton.setVisibility(View.GONE);
        }
    }

}
