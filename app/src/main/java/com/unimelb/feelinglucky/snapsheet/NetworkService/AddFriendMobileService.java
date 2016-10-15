package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;
import com.unimelb.feelinglucky.snapsheet.Bean.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by leveyleonhardt on 10/12/16.
 */

public interface AddFriendMobileService {
    @GET("/users/addmobile")
    Call<User> addFriendMobile(@Query("username") String username, @Query("mobile") String mobile);
}
