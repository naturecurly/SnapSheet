package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.List;

/**
 * Created by mac on 16/10/6.
 */

public class FriendListViewApter extends BaseAdapter {

    private List<WifiP2pDevice> mPeers;
    private Context mContext;
    private LayoutInflater inflater;

    public FriendListViewApter (Context context, List<WifiP2pDevice> peers) {
        mPeers = peers;
        mContext = context;

        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mPeers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPeers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scan_friend_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.scan_friend_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mPeers != null) {
            viewHolder.name.setText(mPeers.get(position).deviceName);
        } else  {
            viewHolder.name.setText("hello");
        }
        return convertView;
    }

    private class ViewHolder{
        TextView name;
    }
}
