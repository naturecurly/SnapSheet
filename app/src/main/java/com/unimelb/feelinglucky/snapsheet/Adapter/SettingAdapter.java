package com.unimelb.feelinglucky.snapsheet.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.Util.LogoutUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.unimelb.feelinglucky.snapsheet.R.id.item_settings_textview;
import static com.unimelb.feelinglucky.snapsheet.R.id.light;

/**
 * Created by leveyleonhardt on 10/12/16.
 */

public class SettingAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater = null;
    private List<String> list = new ArrayList<>();
    private Context context = null;

    public SettingAdapter(Context context) {
        String[] arrays = {"Username", "Birthday", "Mobile Number", "Email", "Log Out"};
        list = Arrays.asList(arrays);
        this.context = context;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = (String) getItem(position);
        convertView = layoutInflater.inflate(R.layout.item_settings, null);
        TextView catagory = (TextView) convertView.findViewById(R.id.item_setting_catagory_textview);
        TextView textView = (TextView) convertView.findViewById(item_settings_textview);
        TextView content = (TextView) convertView.findViewById(R.id.item_setting_content);
        if (position == 0) {
            catagory.setText("MY ACCOUNT");
            textView.setText(list.get(position));
        } else if (position == 4) {
            catagory.setText("ACCOUNT ACTIONS");
            textView.setText(list.get(position));
            content.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogoutUtils.logout(context);
                }
            });
        } else {
            catagory.setVisibility(View.GONE);
            textView.setText(list.get(position));
        }
        return convertView;
    }
}
