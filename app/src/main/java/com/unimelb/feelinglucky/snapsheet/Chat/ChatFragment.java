package com.unimelb.feelinglucky.snapsheet.Chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.feelinglucky.snapsheet.Chat.widget.FriendInfoAdapter;
import com.unimelb.feelinglucky.snapsheet.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by leveyleonhardt on 8/11/16.
 */
public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mChatPresenter;
    private final String TAG = ChatFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] myDataset = {"a", "b", "c"};
        // specify an adapter (see also next example)
        mAdapter = new FriendInfoAdapter(myDataset);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }


    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mChatPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mChatPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "stop");

//        mRecyclerView.notify();
    }
}
