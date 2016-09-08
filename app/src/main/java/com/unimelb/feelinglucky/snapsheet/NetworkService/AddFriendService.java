package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by leveyleonhardt on 9/8/16.
 */
public interface AddFriendService {
    @GET("/users/addfriends")
    Call<ReturnMessage> addFriends(@Query("account") String account, @Query("username") String username);
}
