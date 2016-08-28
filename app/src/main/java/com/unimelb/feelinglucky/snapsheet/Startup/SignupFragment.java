package com.unimelb.feelinglucky.snapsheet.Startup;

import android.content.Context;
import android.content.SharedPreferences;
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

/**
 * Created by leveyleonhardt on 8/27/16.
 */
public class SignupFragment extends Fragment {
    private Button continueButton;
    private EditText emailText;
    private boolean hasInput = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        emailText = (EditText) view.findViewById(R.id.email_edit_text);
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
                    hasInput = false;
                    continueButton.setVisibility(View.GONE);

                } else {
                    hasInput = true;
                    continueButton.setVisibility(View.VISIBLE);
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
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", emailText.getText().toString());
                editor.commit();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.activity_startup_container, new PasswordFragment()).addToBackStack("email").commit();
            }
        });
        return view;
    }
}
