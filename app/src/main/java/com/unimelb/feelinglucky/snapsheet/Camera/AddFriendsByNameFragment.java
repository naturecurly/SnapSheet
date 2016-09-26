package com.unimelb.feelinglucky.snapsheet.Camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.NetworkService.AddFriendService;
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
 * Created by leveyleonhardt on 9/8/16.
 */
public class AddFriendsByNameFragment extends Fragment {
    private SearchView searchView;
    private Button addButton;
    private TextView usernameText;
    private LinearLayout userLayout;
    private String usernameToAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friends_by_name, container, false);
        addButton = (Button) view.findViewById(R.id.add_friend_by_name_button);
        usernameText = (TextView) view.findViewById(R.id.add_friend_by_name_textview);
        userLayout = (LinearLayout) view.findViewById(R.id.add_friend_by_name_layout);
        userLayout.setVisibility(View.GONE);
        searchView = (SearchView) view.findViewById(R.id.fragment_add_friends_search_name);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.length() != 0) {
                    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                    CheckUsernameService checkUsernameService = retrofit.create(CheckUsernameService.class);
                    Call call = checkUsernameService.checkUsername(newText);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                ReturnMessage message = (ReturnMessage) response.body();
                                if (message.isSuccess()) {
                                    usernameToAdd = newText;
                                    userLayout.setVisibility(View.VISIBLE);
                                    usernameText.setText(newText);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });
                }
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                AddFriendService addFriendService = retrofit.create(AddFriendService.class);
                Call call = addFriendService.addFriends(SharedPreferencesUtils.getSharedPreferences(getActivity()).getString("username", null), usernameToAdd);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            ReturnMessage message = (ReturnMessage) response.body();
                            if (message.isSuccess()) {
                                addButton.setText("Added");
                            } else {
                                Toast.makeText(getActivity(), "Add failed", Toast.LENGTH_SHORT).show();
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
