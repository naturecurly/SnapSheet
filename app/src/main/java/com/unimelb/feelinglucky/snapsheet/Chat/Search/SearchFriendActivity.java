package com.unimelb.feelinglucky.snapsheet.Chat.Search;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.EditText;

import com.unimelb.feelinglucky.snapsheet.Chat.widget.FriendNameAdapter;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SortByName;

import java.util.ArrayList;

/**
 * Created by mac on 16/9/6.
 */
public class SearchFriendActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> myDataset;
    private ArrayList<String> mDataSetForDisplay;
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        init();
        mRecyclerView = (RecyclerView) findViewById(R.id.search_friend_recycler);
        mSearchView = (SearchView) findViewById(R.id.search_friend_search);
        mSearchView.setIconifiedByDefault(false);
        ((EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).
                setHintTextColor(ContextCompat.getColor(this, R.color.lower_chat_entrance_blue));

        mSearchView.setOnQueryTextListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new FriendNameAdapter(this, myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void init() {
        myDataset = (ArrayList<String>) DatabaseUtils.fetchFriends(DatabaseInstance.database);
        mDataSetForDisplay = new ArrayList<>();
        myDataset = SortByName.sortByName(myDataset);


    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mDataSetForDisplay.clear();
        if (newText == null || newText.equals("")) {
            mAdapter = new FriendNameAdapter(this, myDataset);
            mRecyclerView.setAdapter(mAdapter);
            return false;
        }
        for (String str : myDataset) {
            if (str.toLowerCase().startsWith(newText.toLowerCase())) {
                mDataSetForDisplay.add(str);
            }
        }
        SortByName.insertHeader(mDataSetForDisplay);
        mAdapter = new FriendNameAdapter(this, mDataSetForDisplay);
        mRecyclerView.setAdapter(mAdapter);
        return false;
    }
}
