package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by leveyleonhardt on 8/31/16.
 */
public interface CheckEmailService {
    @GET("/users/checkemail")
    Call<ReturnMessage> checkEmail(@Query("email") String email);
}
