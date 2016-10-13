package com.unimelb.feelinglucky.snapsheet.Chatroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Bean.Message;
import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.SendMessageService;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Weiwei Cai on 8/11/16.
 */
public class ChatRoomFragment extends Fragment {
    public static final String TAG = ChatRoomFragment.class.getSimpleName();
    private String mChatFriend; // current friend chatting with

    private DrawerLayout mDrawer;
    private Button mDrawerToggle;
    private TextView mChatName;
    private RecyclerView mChat;

    private EditText tvSendMsg;
    private ChatRecyclerViewAdapter mAdapter;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private List<String> messageList;

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
        //Toast.makeText(getContext(), ((SnapSheetActivity)getActivity()).getmChatWith(), Toast.LENGTH_LONG).show();

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
        messageList = new ArrayList<>();
        messageList.add("Test message");
        mAdapter = new ChatRecyclerViewAdapter(getContext(), messageList);
        mChat.setLayoutManager(new LinearLayoutManager(getActivity()));

        mChat.setAdapter(mAdapter);


        tvSendMsg = (EditText) view.findViewById(R.id.chat_input);
        tvSendMsg.setOnEditorActionListener(new getOnEditorActionListener());

        return view;
    }

    public boolean sendMessage(String msg) {
        Message message = new Message();
        message.setContent(msg);
        // type: msg or img
        message.setType(Message.MSG);
        message.setTo(mChatFriend);

        String username = SharedPreferencesUtils.getSharedPreferences(getContext()).getString(SharedPreferencesUtils.USERNAME, "");
        if (username.isEmpty()) {
            Log.e(TAG, "current user owns an empty username, weird");
            return false;
        }
        message.setFrom("xuhui");


        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
        final SendMessageService sendMessageService = retrofit.create(SendMessageService.class);

        Call<ReturnMessage> call = sendMessageService.sendMessage(message);
        call.enqueue(new Callback<ReturnMessage>() {
            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                Log.i(TAG, "success");
                //ReturnMessage rm = response.body();
                //Log.i(TAG, String.valueOf(rm.isSuccess()));
                //Log.i(TAG, rm.getMessage());
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                Log.e(TAG, "send message failed");
            }
        });

        return true;
    }

    public void setChatFriend(String chatFriend) {
        mChatFriend = chatFriend;
        mChatName.setText(chatFriend);
    }

    class getOnEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEARCH:
                    Toast.makeText(getContext(), "You click on search, text:"+v.getText(), Toast.LENGTH_LONG).show();
                    break;

                case EditorInfo.IME_ACTION_NEXT:
                    Toast.makeText(getContext(), "You click on next, text:"+v.getText(), Toast.LENGTH_LONG).show();
                    break;
                case EditorInfo.IME_ACTION_SEND:
                    Toast.makeText(getContext(), "You click on send, text:"+v.getText(), Toast.LENGTH_LONG).show();
                    break;
                case EditorInfo.IME_ACTION_DONE:

                    String msg = v.getText().toString();
                    if (sendMessage(msg)) {
                        messageList.add(msg);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "send message failed", Toast.LENGTH_LONG).show();
                    }
                    v.setText("");

                    break;
                default:
                    break;
            }
            return false;
        }

    }
}
