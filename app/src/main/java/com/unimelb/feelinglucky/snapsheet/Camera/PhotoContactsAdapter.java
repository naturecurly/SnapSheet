package com.unimelb.feelinglucky.snapsheet.Camera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unimelb.feelinglucky.snapsheet.Bean.Contact;
import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.NetworkService.AddFriendMobileService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mac on 16/10/9.
 */

public class PhotoContactsAdapter extends BaseAdapter {
    private List<Contact> mContacts;
    private LayoutInflater inflater;
    private Context mContext;

    public PhotoContactsAdapter(Context context, List<Contact> contacts) {
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
        } else {
            viewHolder = (PhotoContactsAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mContacts.get(position).getUsername());
        if (DatabaseUtils.isFriend(DatabaseInstance.database, mContacts.get(position).getUsername())) {
            viewHolder.add.setText("Added");
        } else {
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                    AddFriendMobileService addFriendService = retrofit.create(AddFriendMobileService.class);
                    Call call = addFriendService.addFriendMobile(SharedPreferencesUtils.getSharedPreferences(mContext).getString("username", null), mContacts.get(position).getMobile());
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                User friend = (User) response.body();
                                if (friend != null) {
                                    Toast.makeText(mContext, "Send", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "Add failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(mContext, "Add failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });
                }
            });
        }
        viewHolder.mobile.setText(mContacts.get(position).getMobile());

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView mobile;
        Button add;
    }
}
