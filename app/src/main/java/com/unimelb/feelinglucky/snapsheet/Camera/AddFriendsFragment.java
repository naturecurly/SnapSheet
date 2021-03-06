package com.unimelb.feelinglucky.snapsheet.Camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;


/**
 * Created by leveyleonhardt on 9/7/16.
 */
public class AddFriendsFragment extends Fragment {
    private ListView listView;
    private final int[] images = {R.drawable.ic_search, R.drawable.ic_contact, R.drawable.ic_wifi, R.drawable.ic_share};
    private final String[] texts = {"Add by Username", "Add from Address Book", "Add Nearby", "Share Username"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_add_friends, container, false);
        listView = (ListView) view.findViewById(R.id.add_friend_listview);
        listView.addFooterView(new View(getActivity()), null, true);
        listView.addHeaderView(new View(getActivity()), null, true);
        listView.setAdapter(new MyAdapter());
        final AddFriendNearByFragment nearByFragment = new AddFriendNearByFragment();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_profile_container, new AddFriendsByNameFragment()).addToBackStack("addFriends").commit();
                } else if (position == 2) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_profile_container, new AddFriendByContactFragment()).addToBackStack("addFriends").commit();
                } else if (position == 3) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_profile_container, nearByFragment).addToBackStack("addFriends").commit();
                } else if (position == 4) {
                    share();
                }
            }
        });
        setContactPermission();
        return view;
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_info_with_others ,
                SharedPreferencesUtils.getSharedPreferences(getActivity()).getString("username",null)));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_info_with_others)));
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return texts[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            TextView textView;
            convertView = getActivity().getLayoutInflater().inflate(R.layout.add_friends_item, null);
            imageView = (ImageView) convertView.findViewById(R.id.item_icon);
            textView = (TextView) convertView.findViewById(R.id.item_text);
            imageView.setImageResource(images[position]);
            textView.setText(texts[position]);
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (position == 0) {
//                        getFragmentManager().beginTransaction().replace(R.id.fragment_profile_container, new AddFriendsByNameFragment()).addToBackStack("addFriends").commit();
//                    }
//                }
//            });
            return convertView;
        }
    }

    public void setContactPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);
            }
        }
    }
}
