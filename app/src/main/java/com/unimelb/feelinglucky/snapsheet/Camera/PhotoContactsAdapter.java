package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.Bean.Contact;
import com.unimelb.feelinglucky.snapsheet.R;

import java.util.List;

/**
 * Created by mac on 16/10/9.
 */

public class PhotoContactsAdapter extends BaseAdapter {
    private List<Contact> mContacts;
    private LayoutInflater inflater;
    private Context mContext;

    public PhotoContactsAdapter (Context context, List<Contact> contacts) {
        mContacts = contacts;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoContactsAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new PhotoContactsAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.contact_list_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.add_friend_by_contact_name);
            viewHolder.mobile = (TextView) convertView.findViewById(R.id.add_friend_by_contact_mobile);
            viewHolder.add = (Button) convertView.findViewById(R.id.add_friend_by_contact_button);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PhotoContactsAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mContacts.get(position).getUsername());
        viewHolder.mobile.setText(mContacts.get(position).getMobile());
        return convertView;
    }

    private class ViewHolder{
        TextView name;
        TextView mobile;
        Button add;
    }
}
