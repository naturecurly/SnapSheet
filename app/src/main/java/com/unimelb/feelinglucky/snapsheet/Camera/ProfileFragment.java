package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by leveyleonhardt on 8/31/16.
 */
public class ProfileFragment extends Fragment {
    private TextView profileUsername;
    private Button addFriendsButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileUsername = (TextView) view.findViewById(R.id.fragment_profile_username_textview);
        SharedPreferences preferences = SharedPreferencesUtils.getSharedPreferences(getActivity());
        profileUsername.setText(preferences.getString("username", null));
        addFriendsButton = (Button) view.findViewById(R.id.fragment_profile_add_friend_button);
        addFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_profile_container, new AddFriendsFragment()).addToBackStack("profile").commit();

            }
        });
        return view;
    }
}
