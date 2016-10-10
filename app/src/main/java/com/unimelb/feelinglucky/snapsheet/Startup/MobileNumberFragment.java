package com.unimelb.feelinglucky.snapsheet.Startup;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import org.w3c.dom.Text;

/**
 * Created by leveyleonhardt on 10/9/16.
 */

public class MobileNumberFragment extends Fragment {
    private Button continueButton;
    private EditText mobileText;
    private TextView mobileHint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile, container, false);
        mobileHint = (TextView) view.findViewById(R.id.mobile_hint);
        mobileText = (EditText) view.findViewById(R.id.mobile_edit_text);
        continueButton = (Button) view.findViewById(R.id.fragment_mobile_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileText.getText().length() == 0) {
                    mobileHint.setText("Mobile number number cannot be null");
                    mobileHint.setTextColor(Color.RED);
                } else {
                    SharedPreferences sharedPreferences = SharedPreferencesUtils.getSharedPreferences(getActivity());
                    sharedPreferences.edit().putString("mobile", mobileText.getText().toString()).commit();
                }
                getFragmentManager().beginTransaction().replace(R.id.activity_startup_container, new BirthdayFragment()).addToBackStack("mobile").commit();
            }
        });
        return view;
    }
}
