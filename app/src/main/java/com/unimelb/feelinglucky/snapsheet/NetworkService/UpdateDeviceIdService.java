package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by leveyleonhardt on 10/7/16.
 */

public interface UpdateDeviceIdService {
    @FormUrlEncoded
    @POST("/users/updateid")
    Call<User> updateDeviceId(@Field("username") String username, @Field("deviceId") String deviceId);
}
