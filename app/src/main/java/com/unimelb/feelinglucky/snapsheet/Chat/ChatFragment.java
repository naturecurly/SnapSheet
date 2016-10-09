package com.unimelb.feelinglucky.snapsheet.Chat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.Chat.Search.SearchFriendActivity;
import com.unimelb.feelinglucky.snapsheet.Chat.widget.FriendInfoAdapter;
import com.unimelb.feelinglucky.snapsheet.Database.UserDataOpenHelper;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SnapSheetActivity;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by leveyleonhardt on 8/11/16.
 */
public class ChatFragment extends Fragment implements ChatContract.View {

    private static final int RESULTID = 1;
    private ChatContract.Presenter mChatPresenter;
    private final String TAG = ChatFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTextView;
    private PointF origin;
    private SearchView mSearchView;
    private Button mImageButton;
    private TextView mTitle;

    private String[] myDataset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        myDataset = DatabaseUtils.loadFriendsWithPriority(new UserDataOpenHelper(getContext()).getWritableDatabase());

        // specify an adapter (see also next example)
        mAdapter = new FriendInfoAdapter(getContext(),myDataset);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mTextView = (TextView) view.findViewById(R.id.fragment_chat_textview);
        mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);

                PointF current = new PointF(event.getX(), event.getY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        origin = current;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (current.x - origin.x <= 0) {
                            v.getParent().requestDisallowInterceptTouchEvent(false);

                        } else {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        }

                        break;
                }

                return true;
            }
        });

        mImageButton = (Button) view.findViewById(R.id.fragment_chat_with_friend);
        mSearchView = (SearchView) view.findViewById(R.id.fragment_chat_searchview);
        mTitle = (TextView) view.findViewById(R.id.fragment_chat_title);
        int searchID = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) mSearchView.findViewById(searchID);
        v.setImageResource(R.drawable.ic_search_black_24dp);

        int searchSrcTextId = android.support.v7.appcompat.R.id.search_src_text;
        EditText searchEditText = (EditText) mSearchView.findViewById(searchSrcTextId);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.LTGRAY);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchFriendActivity.class);
                startActivityForResult(intent, RESULTID);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFriend(newText);
                return true;
            }
        });

        return view;
    }

    private void searchFriend(String name) {
        List<String> names = new ArrayList<>();
        for (String str : myDataset){
            if (str.startsWith(name)) {
                names.add(str);
            }
        }
        String[] search = new String[names.size()];

        mAdapter = new FriendInfoAdapter(getActivity(), names.toArray(search));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void refreshFriendList () {
        myDataset = DatabaseUtils.loadFriendsWithPriority(new UserDataOpenHelper(getContext()).getWritableDatabase());
        mAdapter = new FriendInfoAdapter(getContext(),myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTID) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {
                refreshFriendList ();
                ((SnapSheetActivity)getActivity()).setViewPagerItem(0);
                ((SnapSheetActivity)getActivity()).setmChatWith(data.getStringExtra("id"));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mChatPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }
}
