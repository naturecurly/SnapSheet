package com.unimelb.feelinglucky.snapsheet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leveyleonhardt on 10/6/16.
 */

public class FriendAdapter extends BaseAdapter {
    private List<String> friendList = new ArrayList<>();
    private Context context = null;
    private LayoutInflater layoutInflater = null;

    public FriendAdapter(Context context, List<String> friendList) {
        this.friendList = friendList;
        this.context = context;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return friendList.size();
    }


    @Override
    public Object getItem(int position) {
        return friendList.size() == 0 ? null : friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        String username = (String) getItem(position);
        viewHolder = new ViewHolder();
        convertView = layoutInflater.inflate(R.layout.item_friend_list, null);
        viewHolder.catalog = (TextView) convertView.findViewById(R.id.item_friend_list_catalog);
        viewHolder.username = (TextView) convertView.findViewById(R.id.item_friend_list_textview);
        convertView.setTag(viewHolder);
        if (showCatagory(position)) {
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(friendList.get(position).substring(0, 1).toUpperCase());
            viewHolder.username.setText(friendList.get(position));
        } else {
            viewHolder.catalog.setVisibility(View.GONE);
            viewHolder.username.setText(friendList.get(position));

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView catalog = null;
        TextView username = null;

    }

    private boolean showCatagory(int position) {
        if (position == 0) {
            return true;
        } else {
            String last = friendList.get(position - 1);
            String current = friendList.get(position);
            if (last.substring(0, 1).toLowerCase().equals(current.substring(0, 1).toLowerCase())) {
                return false;
            } else {
                return true;
            }
        }
    }
}
