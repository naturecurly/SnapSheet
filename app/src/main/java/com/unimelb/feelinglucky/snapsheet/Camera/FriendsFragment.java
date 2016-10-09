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
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.Database.UserDataOpenHelper;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leveyleonhardt on 9/27/16.
 */

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SQLiteDatabase mDatabase;
    private List<String> friendData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new UserDataOpenHelper(getActivity()).getWritableDatabase();
        friendData = DatabaseUtils.fetchFriends(mDatabase);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_friend_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new myAdapter(friendData));
        return view;
    }

    private class myAdapter extends RecyclerView.Adapter<myViewHolder> {
        private List<String> data;

        public myAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_friend_list, parent, false);
            myViewHolder mViewHolder = new myViewHolder(v);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    private class myViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public myViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_friend_list_textview);
        }
    }
}
