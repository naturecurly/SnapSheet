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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leveyleonhardt on 8/28/16.
 */
public class PasswordFragment extends Fragment {
    private Button continueButton;
    private EditText passwordText;
    private Pattern checkPassword;
    private TextView passwordHint;
    private ColorStateList defaultColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPassword = Pattern.compile("^[A-Z0-9._%+-]{8,}$", Pattern.CASE_INSENSITIVE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        passwordHint = (TextView) view.findViewById(R.id.password_hint);
        defaultColor = passwordHint.getTextColors();
        continueButton = (Button) view.findViewById(R.id.fragment_password_continue_button);
        continueButton.setVisibility(View.GONE);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", passwordText.getText().toString());
                editor.commit();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.activity_startup_container, new UsernameFragment()).addToBackStack("password").commit();
            }
        });
        passwordText = (EditText) view.findViewById(R.id.password_edit_text);
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    continueButton.setVisibility(View.GONE);
                    passwordHint.setText(R.string.signup_password_hint);
                    passwordHint.setTextColor(defaultColor);

                } else {
                    Matcher matcher = checkPassword.matcher(s);
                    if (matcher.matches()) {
                        passwordHint.setText(R.string.signup_password_hint);
                        passwordHint.setTextColor(defaultColor);

                        continueButton.setVisibility(View.VISIBLE);

                    } else {
                        continueButton.setVisibility(View.GONE);
                        passwordHint.setText(R.string.password_invalid);
                        passwordHint.setTextColor(Color.RED);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
}
