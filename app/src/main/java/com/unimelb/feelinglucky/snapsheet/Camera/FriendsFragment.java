package com.unimelb.feelinglucky.snapsheet.Camera;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.Adapter.FriendAdapter;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.UsernameSortUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leveyleonhardt on 9/27/16.
 */

public class FriendsFragment extends Fragment {

    private ListView listView;
    private SQLiteDatabase mDatabase;
    private List<String> friendData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendData = DatabaseUtils.fetchFriends(DatabaseInstance.database);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        listView = (ListView) view.findViewById(R.id.fragment_friend_list_view);
        listView.setAdapter(new FriendAdapter(getActivity(), UsernameSortUtils.sortUsername(DatabaseUtils.fetchFriends(DatabaseInstance.database))));
        return view;
    }

}
