package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by leveyleonhardt on 8/30/16.
 */
public interface CheckUsernameService {
    @GET("/users/checkusername")
    Call<ReturnMessage> checkUsername(@Query("username") String username);
}
