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
import com.unimelb.feelinglucky.snapsheet.Util.SortByName;

import java.util.ArrayList;

/**
 * Created by mac on 16/9/6.
 */
public class SearchFriendActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> myDataset;
    private SearchView mSearchView;

    private String[] test = {"aasd", "b", "cdds asd", "a", "b", "c", "A", "b", "c", "a", "b", "c", "a", "B", "c", "a", "b", "c", "a", "b", "2", "c", "a", "1", "b", "c"};

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


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FriendNameAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void init() {


        myDataset = new ArrayList();
        for (String str : test) {
            myDataset.add(str);
        }

        myDataset = SortByName.sortByName(myDataset);
    }
}
