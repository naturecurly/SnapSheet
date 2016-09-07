package com.unimelb.feelinglucky.snapsheet.Startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.NetworkService.CheckEmailService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 8/27/16.
 */
public class SignupFragment extends Fragment {
    private Button continueButton;
    private EditText emailText;
    private Pattern checkEmail;
    private TextView emailHint;
    private ColorStateList defaultColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        emailText = (EditText) view.findViewById(R.id.email_edit_text);
        emailHint = (TextView) view.findViewById(R.id.email_hint);
        defaultColor = emailHint.getTextColors();
        continueButton = (Button) view.findViewById(R.id.fragment_signup_continue_button);
        continueButton.setVisibility(View.GONE);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.activity_startup_container, new PasswordFragment()).addToBackStack("email").commit();
            }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    continueButton.setVisibility(View.GONE);
                    emailHint.setText(R.string.signup_email_hint);
                    emailHint.setTextColor(defaultColor);

                } else {
                    Matcher matcher = checkEmail.matcher(s);
                    if (matcher.matches()) {
                        emailHint.setText(R.string.signup_email_hint);
                        continueButton.setVisibility(View.VISIBLE);
                        emailHint.setTextColor(defaultColor);


                    } else {
                        emailHint.setText(R.string.invalid_email);
                        emailHint.setTextColor(Color.RED);
                        continueButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        continueButton = (Button) view.findViewById(R.id.fragment_signup_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                CheckEmailService checkEmailService = retrofit.create(CheckEmailService.class);
                Call call = checkEmailService.checkEmail(emailText.getText().toString());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        ReturnMessage message = (ReturnMessage) response.body();
                        if (response.isSuccessful()) {
                            //The server does not have this email
                            if (message.isSuccess() == false) {
                                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", emailText.getText().toString());
                                editor.commit();
                                FragmentManager fm = getFragmentManager();
                                fm.beginTransaction().replace(R.id.activity_startup_container, new PasswordFragment()).addToBackStack("email").commit();
                            } else {
                                emailHint.setText("This email existed.");
                                emailHint.setTextColor(Color.RED);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
        return view;
    }
}
