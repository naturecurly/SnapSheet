
package com.unimelb.feelinglucky.snapsheet.Camera;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
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
import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.UpdateDeviceIdService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.UploadImageService;
import com.unimelb.feelinglucky.snapsheet.R;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;
import com.unimelb.feelinglucky.snapsheet.Util.DatabaseUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SendMessageUtils;
import com.unimelb.feelinglucky.snapsheet.Util.SharedPreferencesUtils;
import com.unimelb.feelinglucky.snapsheet.Util.UsernameSortUtils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 10/6/16.
 */

public class SendImageFriendListFragment extends Fragment {

    private ListView listView;
    private byte[] mImage;
    private int mTime;

    public static SendImageFriendListFragment newInstance(byte[] image, int time) {
        Bundle args = new Bundle();
        args.putByteArray("image", image);
        args.putInt("time", time);
        SendImageFriendListFragment fragment = new SendImageFriendListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImage = getArguments().getByteArray("image");
        mTime = getArguments().getInt("time");
        Log.i("image", mImage.length + "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgent_send_image_friend_list, container, false);
        listView = (ListView) view.findViewById(R.id.fragment_send_image_friend_listview);
        listView.setAdapter(new FriendAdapter(getActivity(), UsernameSortUtils.sortUsername(DatabaseUtils.fetchFriends(DatabaseInstance.database))));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
                UploadImageService uploadImageService = retrofit.create(UploadImageService.class);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mImage);
                MultipartBody.Part multiPart = MultipartBody.Part.createFormData("image", "undefined", requestBody);
                Call call = uploadImageService.uploadImage(multiPart);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            ReturnMessage returnMessage = (ReturnMessage) response.body();
                            SendMessageUtils.sendImageMessage(getActivity(), SharedPreferencesUtils.getSharedPreferences(getActivity()).getString(SharedPreferencesUtils.USERNAME, ""), item, Integer.toString(mTime), returnMessage.getMessage());
                            Log.i("sendImage", returnMessage.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("sendImage", "500");
                    }
                });
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
