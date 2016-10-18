package com.unimelb.feelinglucky.snapsheet.Chatroom;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.unimelb.feelinglucky.snapsheet.Bean.ReturnSendMessage;
import com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.SendMessageService;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Weiwei Cai on 8/11/16.
 */
public class ChatRoomFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = ChatRoomFragment.class.getSimpleName();
    public static final int CHATROOMFRAGMENT_LOADER = 0;
    private String mChatFriend; // current friend chatting with

    private DrawerLayout mDrawer;
    private Button mDrawerToggle;
    private Button mGoBack;
    private Button btShutter;
    private TextView mChatName;
    private RecyclerView mChat;

    private EditText tvSendMsg;
    private ChatRecyclerViewAdapter mAdapter;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private List<Message> messageList;
    private Set<Integer> messageIdSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom, container, false);

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

        mGoBack = (Button) view.findViewById(R.id.chat_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SnapSheetActivity) getActivity()).setViewPagerItem(1);
            }
        });

        btShutter = (Button) view.findViewById(R.id.chat_camera);
        btShutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SnapSheetActivity) getActivity()).setViewPagerItem(2);
            }
        });

        mChat = (RecyclerView) view.findViewById(R.id.chat_room_body);
        messageList = new ArrayList<>();
        mAdapter = new ChatRecyclerViewAdapter(getContext(), messageList);
        mChat.setLayoutManager(new LinearLayoutManager(getActivity()));

        mChat.setAdapter(mAdapter);
        messageIdSet = new HashSet<>();


        tvSendMsg = (EditText) view.findViewById(R.id.chat_input);
        tvSendMsg.setOnEditorActionListener(new getOnEditorActionListener());

        return view;
    }

    private  Message buildMessage(String from, String to, String type, String msg) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        // type: msg, img, read
        message.setType(type);
        message.setContent(msg);

        // add the dummy data
        message.setStatus("0");
        message.setLive_time("3");
        return message;
    }

    private Message buildMessage(String type, String msg) {
        String username = SharedPreferencesUtils.getSharedPreferences(getContext()).getString(SharedPreferencesUtils.USERNAME, "");
        if (username.isEmpty()) {
            Log.e(TAG, "current user owns an empty username, weird");
            return null;
        }
        String from = username;
        String to = mChatFriend;
        return buildMessage(from, to, type, msg);
    }

    private Message buildMessage(String msg) {
        String type = Message.MSG;
        return buildMessage(type, msg);
    }

    public boolean sendMessage(Message message) {

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
        final SendMessageService sendMessageService = retrofit.create(SendMessageService.class);

        Call<ReturnSendMessage> call = sendMessageService.sendMessage(message);
        call.enqueue(new Callback<ReturnSendMessage>() {
            @Override
            public void onResponse(Call<ReturnSendMessage> call, Response<ReturnSendMessage> response) {
                Log.i(TAG, "success");
                // if successfully sent message, store this message into database
                // Note: only store 'msg' type messages, do not store 'read' type message
                if (message.getType().equalsIgnoreCase("msg")) {
                    Uri chatMessageUri = SnapSeetDataStore.ChatMessage.CONTENT_URI.buildUpon().build();
                    ContentValues values = DatabaseUtils.buildChatMessage(message);
                    getContext().getContentResolver().insert(chatMessageUri, values);
                }
            }

            @Override
            public void onFailure(Call<ReturnSendMessage> call, Throwable t) {
                Log.e(TAG, "send message failed");
                Toast.makeText(getContext(), "OnFailure: send message failed", Toast.LENGTH_LONG).show();
            }
        });

        return true;
    }

    public void setChatFriend(String chatFriend) {
        mChatFriend = chatFriend;
        mChatName.setText(chatFriend);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CHATROOMFRAGMENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader");
        // query those message that from chat friend
        // message to the friend will be loaded in enterroom method
        Uri chatMessageByUsername = SnapSeetDataStore.ChatMessage.CONTENT_URI_FROM_USER.buildUpon().appendPath(mChatFriend).build();
        return new CursorLoader(getActivity(),
                chatMessageByUsername,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // mChatFriend is null means that we are not in the chat room now
        if (mChatFriend == null) {
            return;
        }

        Log.i(TAG, "onLoadFinished");
        int position = messageList.size();
        while (data.moveToNext()) {
            Message message = DatabaseUtils.buildMessageFromCursor(data);
            // only msg type message will be added because they will be cleared after queried and won't be duplicated
            // however, we should add new img type message too.
            int id = data.getInt(data.getColumnIndex(SnapSeetDataStore.ChatMessage._ID));
            Log.i(TAG, "messageId: " + id);
            Log.i(TAG, "message type: " + message.getType());


            if (!messageIdSet.contains(id)) {
                Log.i(TAG, "from: " + message.getFrom());
                Log.i(TAG, "to: " + message.getTo());
                Log.i(TAG, "content: " + message.getContent());
                Log.i(TAG, "type: " + message.getType());
                Log.i(TAG, "live_time: " + message.getLive_time());
                Log.i(TAG, "status: " + message.getStatus());

                messageList.add(message);
            }

            // special case for updated message in the database
            if (messageIdSet.contains(id) && message.getType().equalsIgnoreCase(Message.IMG1)) {
                // TODO: it is not a good to iterate the whole list to update just one item every time
                String status = message.getStatus();
                Log.i(TAG, "message status: " + message.getStatus());
                int pos = 0;
                for (Message m : messageList) {
                    Log.i(TAG, "m id: " + m.getLocalId());
                    if (m.getLocalId() == id && !m.getStatus().equalsIgnoreCase(status)) {
                        Log.i(TAG, "m status before: " + m.getStatus());

                        m.setStatus(status);
                        Log.i(TAG, "m status after: " + m.getStatus());
                        mAdapter.notifyItemChanged(pos);
                    }
                    pos++;
                }
            }
        }

        int end = messageList.size();
        mAdapter.notifyItemRangeChanged(position, end-position);
        mChat.scrollToPosition(end-1);

        // when queried, it means that messages from chat friend are read, so they should be removed from the database.
        // but do not delete messages that I sent to this friend.
        // delete will not invoke data change, hence recycle view will keep the messages as long as staying in the chatting room
        Uri chatMessageWithUserUri = SnapSeetDataStore.ChatMessage.CONTENT_URI_FROM_USER_MSG.buildUpon().appendEncodedPath(mChatFriend).build();
        getContext().getContentResolver().delete(chatMessageWithUserUri, null, null);
        // notify these message's sender that I have read them
        Message message = buildMessage(Message.RND, "Read");
        Log.i(TAG, "from: " + message.getFrom());
        Log.i(TAG, "to: " + message.getTo());
        Log.i(TAG, "content: " + message.getContent());
        Log.i(TAG, "type: " + message.getType());

        sendMessage(message);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        // mAdapter.swapCursor(null);
        Log.i(TAG, "onLoaderReset");
    }

    public void enterChatRoom() {
        messageList.clear();

        // get all the message between me and my friends

        Uri uriTo = SnapSeetDataStore.ChatMessage.CONTENT_URI_TO_USER.buildUpon().appendEncodedPath(mChatFriend).build();
        Cursor c = getContext().getContentResolver().query(uriTo, null, null, null, null);
        while (c.moveToNext()) {
            Message message = DatabaseUtils.buildMessageFromCursor(c);
            Log.i(TAG, message.getFrom() + " : " + message.getContent());
            messageIdSet.add(c.getInt(c.getColumnIndex(SnapSeetDataStore.ChatMessage._ID)));
            messageList.add(message);
        }

        Uri uriFrom = SnapSeetDataStore.ChatMessage.CONTENT_URI_FROM_USER.buildUpon().appendEncodedPath(mChatFriend).build();
        Cursor c1 = getContext().getContentResolver().query(uriFrom, null, null, null, null);
        while (c1.moveToNext()) {
            Message message = DatabaseUtils.buildMessageFromCursor(c1);
            Log.i(TAG, message.getFrom() + " : " + message.getContent());
            messageIdSet.add(c1.getInt(c1.getColumnIndex(SnapSeetDataStore.ChatMessage._ID)));
            messageList.add(message);
        }
        mAdapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(CHATROOMFRAGMENT_LOADER, null, this);
    }

    public void leaveChatRoot() {
        mChatFriend = null;
        messageIdSet.clear();

        // delete all status are read or replayed img type local message
        Uri uri = SnapSeetDataStore.ChatMessage.CONTENT_URI_IMG_RND.buildUpon().build();
        getContext().getContentResolver().delete(uri, null, null);
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
                    Message message = buildMessage(msg);
                    Log.i(TAG, "from: " + message.getFrom());
                    Log.i(TAG, "to: " + message.getTo());
                    Log.i(TAG, "content: " + message.getContent());
                    Log.i(TAG, "type: " + message.getType());
                    if (sendMessage(message)) {
                        messageList.add(message);
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
