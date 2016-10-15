package com.unimelb.feelinglucky.snapsheet.Util;

import com.unimelb.feelinglucky.snapsheet.Bean.User;
import com.unimelb.feelinglucky.snapsheet.NetworkService.FetchFriendsService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.SingleInstance.DatabaseInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 10/14/16.
 */

public class FetchFriendUtils {
    public static void fetchFriends(String username) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
        FetchFriendsService fetchFriendsService = retrofit.create(FetchFriendsService.class);
        Call call = fetchFriendsService.fetchFriends(username);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    User[] users = (User[]) response.body();
                    DatabaseUtils.refreshFriendDb(DatabaseInstance.database, users);
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
