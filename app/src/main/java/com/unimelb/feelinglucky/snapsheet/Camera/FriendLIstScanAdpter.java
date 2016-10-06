package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unimelb.feelinglucky.snapsheet.R;

import java.util.List;

/**
 * Created by mac on 16/10/6.
 */

public class FriendLIstScanAdpter extends RecyclerView.Adapter<FriendLIstScanAdpter.ViewHolder> {
    private List<WifiP2pDevice> mPeers;
    private Context mContext;

    public FriendLIstScanAdpter (Context context, List<WifiP2pDevice> peers) {
        mPeers = peers;
        mContext = context;
        Log.i("Una","helo");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mItem;

        public ViewHolder(View v) {
            super(v);
            mItem = (LinearLayout) v;
        }
    }

    @Override
    public FriendLIstScanAdpter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scan_friend_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FriendLIstScanAdpter.ViewHolder vh = new FriendLIstScanAdpter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendLIstScanAdpter.ViewHolder holder, int position) {
        WifiP2pDevice device = mPeers.get(position);
        TextView deviceName = (TextView) holder.mItem.findViewById(R.id.scan_friend_name);
        deviceName.setText(device.deviceName);
        Log.i(String.valueOf(position), device.deviceName);
    }

    @Override
    public int getItemCount() {
        return mPeers.size();
    }
}
