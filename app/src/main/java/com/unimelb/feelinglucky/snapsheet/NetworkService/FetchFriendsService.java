package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by leveyleonhardt on 10/14/16.
 */

public interface FetchFriendsService {
    @FormUrlEncoded
    @POST("users/fetchfriends")
    Call<User[]> fetchFriends(@Field("username") String username);
}
