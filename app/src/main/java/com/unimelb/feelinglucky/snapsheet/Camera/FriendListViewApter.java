package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.NetworkService.AddFriendService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.PrecessingStringUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.scan_friend_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.scan_friend_name);
            viewHolder.add = (Button) convertView.findViewById(R.id.add_friend_near_by_button);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mPeers != null) {
            String friendName = PrecessingStringUtils.getFriendName(mPeers.get(position).deviceName);
            if(DatabaseUtils.isFriend(DatabaseInstance.database, friendName)){
                viewHolder.add.setText("Added");
            }else {
                Toast.makeText(mContext, friendName + "-aaa", Toast.LENGTH_LONG).show();
            }

            viewHolder.name.setText(mPeers.get(position).deviceName);
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext,friendName,Toast.LENGTH_LONG).show();
                    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                    AddFriendService addFriendService = retrofit.create(AddFriendService.class);
                    Call call = addFriendService.addFriends(SharedPreferencesUtils.getSharedPreferences(mContext).getString("username", null), friendName);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                ReturnMessage message = (ReturnMessage) response.body();
                                if (message.isSuccess()) {
                                    DatabaseUtils.insertFriendDb(DatabaseInstance.database, friendName);
                                    Toast.makeText(mContext,"successful",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext,"oop",Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                        }
                    });
                }
            });

        } else  {
            viewHolder.name.setText("hello");
        }
        return convertView;
    }

    private class ViewHolder{
        TextView name;
        Button add;
    }
}
