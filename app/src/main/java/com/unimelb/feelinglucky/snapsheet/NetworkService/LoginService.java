package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by leveyleonhardt on 8/28/16.
 */
public interface LoginService {
    @POST("/users/login")
    Call<User> login(@Body User user);
}
