package com.unimelb.feelinglucky.snapsheet.Util;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.unimelb.feelinglucky.snapsheet.Bean.Message;
import com.unimelb.feelinglucky.snapsheet.Database.SnapSeetDataStore;
import com.unimelb.feelinglucky.snapsheet.NetworkService.NetworkSettings;
import com.unimelb.feelinglucky.snapsheet.NetworkService.SendMessageService;
import com.unimelb.feelinglucky.snapsheet.NetworkService.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 10/16/16.
 */

public class SendMessageUtils {
    public static void sendImageMessage(Context context, String from, String to, String liveTime, String filePath) {
//        SendMessageService sendMessageService = ServiceGenerator.createService(SendMessageService.class);
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(NetworkSettings.baseUrl).build();
        SendMessageService sendMessageService = retrofit.create(SendMessageService.class);
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setType(Message.IMG1);
        message.setLive_time(liveTime);
        message.setContent(filePath);

        Uri uri = SnapSeetDataStore.ChatMessage.CONTENT_URI.buildUpon().build();
        message.setStatus("0");
        ContentValues values = DatabaseUtils.buildChatMessage(message);
        Uri result = context.getContentResolver().insert(uri, values);
        message.setRemoteId(result.getLastPathSegment());
        message.setType(Message.IMG);


        Call call = sendMessageService.sendMessage(message);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("sendImageMessage", "send success");

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("sendImageMessage", "send fail");

            }
        });
    }
}
