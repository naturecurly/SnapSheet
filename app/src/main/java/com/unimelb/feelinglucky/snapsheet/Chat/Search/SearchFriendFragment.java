package com.unimelb.feelinglucky.snapsheet.Chat.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;

/**
 * Created by mac on 16/9/7.
 */
public class SearchFriendFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> myDataset;
    private ArrayList<String> mDataSetForDisplay;
    private SearchView mSearchView;

    private String[] test = {"aasd", "b", "cdds asd","a", "b", "c","A", "b", "c","a", "b", "c","a", "B", "c","a", "b" ,"c","a", "b", "2","c","a","1" ,"b", "c"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_friend, null);

        return view;
    }


}
