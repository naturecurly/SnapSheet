package com.unimelb.feelinglucky.snapsheet.Startup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimelb.feelinglucky.snapsheet.R;

/**
 * Created by leveyleonhardt on 8/27/16.
 */
public class StartupFragment extends Fragment {
    private Button signupButton;
    private Button loginButton;
    private FragmentManager fm;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startup, container, false);
        signupButton = (Button) view.findViewById(R.id.fragment_startup_signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.activity_startup_container, new SignupFragment()).addToBackStack("startup").commit();

            }
        });
        loginButton = (Button) view.findViewById(R.id.fragment_startup_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.activity_startup_container, new LoginFragment()).addToBackStack("startup").commit();
            }
        });
        return view;

    }
}
