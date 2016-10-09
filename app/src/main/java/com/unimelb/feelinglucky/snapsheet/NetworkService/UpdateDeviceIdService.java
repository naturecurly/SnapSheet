package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by leveyleonhardt on 10/7/16.
 */

public interface UpdateDeviceIdService {
    @POST("/users/updateid")
    Call<User> updateDeviceId(@Body String username, @Body String deviceId);
}
