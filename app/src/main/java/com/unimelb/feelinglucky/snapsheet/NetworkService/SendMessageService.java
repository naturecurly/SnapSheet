package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.Message;
import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by leveyleonhardt on 10/8/16.
 */

public interface SendMessageService {
    @POST("/messages/send")
    Call<ReturnMessage> sendMessage(@Body Message message);
}
