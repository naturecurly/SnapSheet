
package com.unimelb.feelinglucky.snapsheet.Camera;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Adapter.FriendAdapter;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;

/**
 * Created by leveyleonhardt on 10/6/16.
 */

public class SendImageFriendListFragment extends Fragment {

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgent_send_image_friend_list, container, false);
        listView = (ListView) view.findViewById(R.id.fragment_send_image_friend_listview);
        listView.setAdapter(new FriendAdapter(getActivity(), DatabaseUtils.fetchFriends(DatabaseInstance.database)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
