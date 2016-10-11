package com.unimelb.feelinglucky.snapsheet.Chatroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiwei Cai on 8/11/16.
 */
public class ChatRoomFragment extends Fragment {
    private DrawerLayout mDrawer;
    private Button mDrawerToggle;
    private TextView mChatName;
    private RecyclerView mChat;
    private ChatRecyclerViewAdapter mAdapter;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom, container, false);
        /*Button bt = (Button) view.findViewById(R.id.test_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), ((SnapSheetActivity)getActivity()).getmChatWith(), Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "want to get the id automatically? Do it in OnPageChangeListener", Toast.LENGTH_LONG).show();
            }
        });*/
        Toast.makeText(getContext(), ((SnapSheetActivity)getActivity()).getmChatWith(), Toast.LENGTH_LONG).show();

        mChatName = (TextView) view.findViewById(R.id.chat_name);
        mChatName.setText(((SnapSheetActivity)getActivity()).getmChatWith());

        // Find our drawer view
        mDrawer = (DrawerLayout) view.findViewById(R.id.chatroom_profile_drawer_layout);

        mDrawerToggle = (Button) view.findViewById(R.id.chat_show_profile);
        mDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

        mChat = (RecyclerView) view.findViewById(R.id.chat_room_body);
        List<String> messageList = new ArrayList<>();
        messageList.add("Test message");
        mAdapter = new ChatRecyclerViewAdapter(getContext(), messageList);
        mChat.setLayoutManager(new LinearLayoutManager(getActivity()));

        mChat.setAdapter(mAdapter);


        return view;
    }
}
